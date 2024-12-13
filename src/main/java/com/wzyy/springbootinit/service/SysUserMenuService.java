package com.wzyy.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wzyy.springbootinit.model.entity.Menu;
import com.wzyy.springbootinit.model.entity.UserMenu;

import java.util.List;

public interface SysUserMenuService extends IService<UserMenu> {

    /**
     * 用户分配权限
     * @param userId 用户id
     * @param menuList 待分配权限id
     * @return 完成状态
     */
    boolean allocationMenus(Long userId, List<Long> menuList);

    boolean allocationMenus(List<Long> userIds, List<Long> menuList);
}
