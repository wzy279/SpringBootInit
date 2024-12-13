package com.wzyy.springbootinit.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzyy.springbootinit.common.ErrorCode;
import com.wzyy.springbootinit.exception.BusinessException;
import com.wzyy.springbootinit.exception.ThrowUtils;
import com.wzyy.springbootinit.mapper.*;
import com.wzyy.springbootinit.model.entity.*;
import com.wzyy.springbootinit.service.SysUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements SysUserRoleService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RoleMapper roleMapper;


    @Override
    public boolean allocationRoles(Long userId, List<Long> menuList) {
        //判断用户是不是存在
        User user = userMapper.selectById(userId);
        ThrowUtils.throwIf(user==null,new BusinessException(ErrorCode.PARAMS_ERROR,"请求用户不存在！"));
        //判断权限是不是都存在
        QueryWrapper<Role> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper.in("id",menuList);
        long count = roleMapper.selectCount(menuQueryWrapper);
        ThrowUtils.throwIf(count!=menuList.size(),new BusinessException(ErrorCode.PARAMS_ERROR,"权限不存在！"));
        //拿到目前已经分配的
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id",userId);
        List<UserRole> list = this.list(userRoleQueryWrapper);
        List<Long> inDataBase = list.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        //需要删除的权限：在inDataBase但是不在menuList
        List<Long> needRemove= inDataBase.stream().filter(item->!menuList.contains(item)).collect(Collectors.toList());
        //需要新增的权限：在menuList里面但是不在inDataBase
        List<Long> needAdd = menuList.stream().filter(item->!inDataBase.contains(item)).collect(Collectors.toList());
        //删除权限
        boolean b = this.removeBatchByIds(needRemove);
        ThrowUtils.throwIf(!b,new BusinessException(ErrorCode.SYSTEM_ERROR));
        List<UserRole> userRoleList = needAdd.stream()
                .map(menuId -> {
                    UserRole userRole = new UserRole();
                    userRole.setRoleId(menuId);
                    userRole.setUserId(userId);
                    return userRole;
                })
                .collect(Collectors.toList());
        //保存权限
        boolean b1 = this.saveBatch(userRoleList);
        ThrowUtils.throwIf(!b1,new BusinessException(ErrorCode.SYSTEM_ERROR));
        StpUtil.kickout(userId);
        return true;
    }

    @Override
    public boolean allocationRoles(List<Long> userIds, List<Long> menuList) {
        for(Long userId:userIds){
            boolean b = this.allocationRoles(userId, menuList);
        }
        return true;
    }

}
