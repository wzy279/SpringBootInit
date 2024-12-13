package com.wzyy.springbootinit.controller;


import com.wzyy.springbootinit.common.BaseResponse;
import com.wzyy.springbootinit.common.ResultUtils;
import com.wzyy.springbootinit.model.dto.menu.AllocationMenu;
import com.wzyy.springbootinit.model.dto.role.AllocationRole;
import com.wzyy.springbootinit.model.entity.Menu;
import com.wzyy.springbootinit.model.entity.Role;
import com.wzyy.springbootinit.service.SysMenuService;
import com.wzyy.springbootinit.service.SysRoleService;
import com.wzyy.springbootinit.service.SysUserMenuService;
import com.wzyy.springbootinit.service.SysUserRoleService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/role")
@Slf4j
@Api(tags = "用户角色相关操作")
public class RoleController {

    @Autowired
    SysUserRoleService sysUserRoleService;

    @Autowired
    SysRoleService sysRoleService;

    //增删改查角色

    //给某个用户赋值某些权限
    @PostMapping("allocationAll")
    public BaseResponse<Boolean> allocationRoles(AllocationRole allocationRole) {
        List<Long> userIds = allocationRole.getUserId();
        List<Long> menuIds = allocationRole.getRole();
        boolean b = sysUserRoleService.allocationRoles(userIds, menuIds);
        return ResultUtils.success(b);
    }


    //增删改查权限
    public BaseResponse<Boolean> addRole(Role role){
        return ResultUtils.success(sysRoleService.save(role));
    }


}
