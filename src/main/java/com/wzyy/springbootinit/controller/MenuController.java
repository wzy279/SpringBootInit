package com.wzyy.springbootinit.controller;


import com.wzyy.springbootinit.common.BaseResponse;
import com.wzyy.springbootinit.common.ResultUtils;
import com.wzyy.springbootinit.exception.ThrowUtils;
import com.wzyy.springbootinit.model.dto.menu.AllocationMenu;
import com.wzyy.springbootinit.model.entity.Menu;
import com.wzyy.springbootinit.service.SysMenuService;
import com.wzyy.springbootinit.service.SysUserMenuService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menu")
@Slf4j
@Api(tags = "用户权限相关操作")
public class MenuController {

    @Autowired
    SysUserMenuService sysUserMenuService;

    @Autowired
    SysMenuService sysMenuService;

    //增删改查角色

    //给某个用户赋值某些权限
    @PostMapping("allocationAll")
    public BaseResponse<Boolean> allocationMenus(AllocationMenu allocationMenu) {
        List<Long> userIds = allocationMenu.getUserId();
        List<Long> menuIds = allocationMenu.getMenuId();
        boolean b = sysUserMenuService.allocationMenus(userIds, menuIds);
        return ResultUtils.success(b);
    }


    //增删改查权限
    public BaseResponse<Boolean> addMenu(Menu menu){
        return ResultUtils.success(sysMenuService.save(menu));
    }



}
