package com.wzyy.springbootinit.config;

import cn.dev33.satoken.stp.StpInterface;
import com.wzyy.springbootinit.mapper.UserMenuMapper;
import com.wzyy.springbootinit.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限加载接口实现类
 */
@Component    // 保证此类被 SpringBoot 扫描，完成 Sa-Token 的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    UserRoleMapper userRoleMapper;

    @Autowired
    UserMenuMapper userMenuMapper;


    /**
     * 返回一个账号所拥有的权限码集合 
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return userMenuMapper.selectPermsByUserId((Long) loginId);
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return userRoleMapper.selectRoleCodeByUserId((Long)loginId);
    }

}