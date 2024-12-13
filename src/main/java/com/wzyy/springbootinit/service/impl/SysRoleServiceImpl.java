package com.wzyy.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzyy.springbootinit.common.ErrorCode;
import com.wzyy.springbootinit.exception.BusinessException;
import com.wzyy.springbootinit.exception.ThrowUtils;
import com.wzyy.springbootinit.mapper.RoleMapper;
import com.wzyy.springbootinit.model.entity.Menu;
import com.wzyy.springbootinit.model.entity.Role;
import com.wzyy.springbootinit.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SysRoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements SysRoleService {

    @Override
    public boolean save(Role entity) {
        String roleName = entity.getRoleName();
        String roleCode = entity.getRoleCode();
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_name",roleName).or().eq("role_code",roleCode);
        long count = this.count(queryWrapper);
        ThrowUtils.throwIf(count>0,new BusinessException(ErrorCode.PARAMS_ERROR,"已存在相同的角色名称或编码！"));
        return super.save(entity);
    }

}
