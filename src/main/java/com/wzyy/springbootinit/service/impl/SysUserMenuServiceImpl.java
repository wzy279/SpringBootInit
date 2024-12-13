package com.wzyy.springbootinit.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzyy.springbootinit.common.ErrorCode;
import com.wzyy.springbootinit.exception.BusinessException;
import com.wzyy.springbootinit.exception.ThrowUtils;
import com.wzyy.springbootinit.mapper.MenuMapper;
import com.wzyy.springbootinit.mapper.UserMapper;
import com.wzyy.springbootinit.mapper.UserMenuMapper;
import com.wzyy.springbootinit.model.entity.Menu;
import com.wzyy.springbootinit.model.entity.User;
import com.wzyy.springbootinit.model.entity.UserMenu;
import com.wzyy.springbootinit.service.SysMenuService;
import com.wzyy.springbootinit.service.SysUserMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class SysUserMenuServiceImpl extends ServiceImpl<UserMenuMapper, UserMenu> implements SysUserMenuService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    MenuMapper menuMapper;


    @Override
    public boolean allocationMenus(Long userId, List<Long> menuList) {
        //判断用户是不是存在
        User user = userMapper.selectById(userId);
        ThrowUtils.throwIf(user==null,new BusinessException(ErrorCode.PARAMS_ERROR,"请求用户不存在！"));
        //判断权限是不是都存在
        QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper.in("id",menuList);
        long count = menuMapper.selectCount(menuQueryWrapper);
        ThrowUtils.throwIf(count!=menuList.size(),new BusinessException(ErrorCode.PARAMS_ERROR,"权限不存在！"));
        //拿到目前已经分配的
        QueryWrapper<UserMenu> userMenuQueryWrapper = new QueryWrapper<>();
        userMenuQueryWrapper.eq("user_id",userId);
        List<UserMenu> list = this.list(userMenuQueryWrapper);
        List<Long> inDataBase = list.stream().map(UserMenu::getMenuId).collect(Collectors.toList());
        //需要删除的权限：在inDataBase但是不在menuList
        List<Long> needRemove= inDataBase.stream().filter(item->!menuList.contains(item)).collect(Collectors.toList());
        //需要新增的权限：在menuList里面但是不在inDataBase
        List<Long> needAdd = menuList.stream().filter(item->!inDataBase.contains(item)).collect(Collectors.toList());
        //删除权限
        boolean b = this.removeBatchByIds(needRemove);
        ThrowUtils.throwIf(!b,new BusinessException(ErrorCode.SYSTEM_ERROR));
        List<UserMenu> userMenuList = needAdd.stream()
                .map(menuId -> {
                    UserMenu userMenu = new UserMenu();
                    userMenu.setMenuId(menuId);
                    userMenu.setUserId(userId);
                    return userMenu;
                })
                .collect(Collectors.toList());
        //保存权限
        boolean b1 = this.saveBatch(userMenuList);
        ThrowUtils.throwIf(!b1,new BusinessException(ErrorCode.SYSTEM_ERROR));
        StpUtil.kickout(userId);
        return true;
    }

    @Override
    public boolean allocationMenus(List<Long> userIds, List<Long> menuList) {
        for(Long userId:userIds){
            boolean b = this.allocationMenus(userId, menuList);
        }
        return true;
    }

}
