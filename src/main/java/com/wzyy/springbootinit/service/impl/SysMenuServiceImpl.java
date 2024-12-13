package com.wzyy.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzyy.springbootinit.common.ErrorCode;
import com.wzyy.springbootinit.exception.BusinessException;
import com.wzyy.springbootinit.exception.ThrowUtils;
import com.wzyy.springbootinit.mapper.MenuMapper;
import com.wzyy.springbootinit.mapper.UserMapper;
import com.wzyy.springbootinit.mapper.UserMenuMapper;
import com.wzyy.springbootinit.model.dto.menu.AllocationMenu;
import com.wzyy.springbootinit.model.entity.Menu;
import com.wzyy.springbootinit.model.entity.User;
import com.wzyy.springbootinit.model.entity.UserMenu;
import com.wzyy.springbootinit.service.SysMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class SysMenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements SysMenuService {


    @Override
    public boolean save(Menu entity) {
        String menuName = entity.getMenuName();
        String perms = entity.getPerms();
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("menu_name",menuName).or().eq("perms",perms);
        long count = this.count(queryWrapper);
        ThrowUtils.throwIf(count>0,new BusinessException(ErrorCode.PARAMS_ERROR,"已存在相同的权限名称或编码！"));
        return super.save(entity);
    }
}
