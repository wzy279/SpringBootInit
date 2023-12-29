package com.wzyy.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzyy.springbootinit.common.ErrorCode;
import com.wzyy.springbootinit.exception.ThrowUtils;
import com.wzyy.springbootinit.mapper.LoginMessageMapper;
import com.wzyy.springbootinit.model.dto.loginMessage.LoginMessageQueryRequest;
import com.wzyy.springbootinit.model.entity.LoginMessage;
import com.wzyy.springbootinit.model.vo.LoginMessageV0;
import com.wzyy.springbootinit.service.LoginMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description 登录消息
 * @author: Wangzhaoyi
 * @create: 2023-12-29 16:08
 **/
@Service
@Slf4j
public class LoginMessageServiceImpl extends ServiceImpl<LoginMessageMapper, LoginMessage> implements LoginMessageService {


    /**
     * 获取登录消息列表
     *
     * @param request 登录消息查询请求
     * @return 登录消息列表
     */
    @Override
    public List<LoginMessageV0> getLoginMessageList(LoginMessageQueryRequest request) {
        ThrowUtils.throwIf(request==null||request.getUserId()==null, ErrorCode.PARAMS_ERROR,"参数不能为空");
        QueryWrapper<LoginMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",request.getUserId());
        List<LoginMessage> list = this.page(new Page<>(request.getCurrent(), request.getPageSize()), queryWrapper).getRecords();

        return list.stream().map(this::getLoginMessageVo).collect(Collectors.toList());
    }


    public LoginMessageV0 getLoginMessageVo(LoginMessage loginMessage){
        if(loginMessage==null){
            return null;
        }
        LoginMessageV0 loginMessageV0 = new LoginMessageV0();
        BeanUtils.copyProperties(loginMessage,loginMessageV0);
        return loginMessageV0;
    }
}
