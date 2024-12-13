package com.wzyy.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wzyy.springbootinit.model.entity.UserMenu;
import com.wzyy.springbootinit.model.entity.UserRole;

import java.util.List;

public interface SysUserRoleService extends IService<UserRole> {

    /**
     * 用户分配角色
     * @param userId 用户id
     * @param roleList 待分配权限id
     * @return 完成状态
     */
    boolean allocationRoles(Long userId, List<Long> roleList);

    boolean allocationRoles(List<Long> userIds, List<Long> roleList);
}
