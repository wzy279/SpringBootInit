package com.wzyy.springbootinit.service;

import com.wzyy.springbootinit.model.dto.loginMessage.LoginMessageQueryRequest;
import com.wzyy.springbootinit.model.entity.LoginMessage;
import com.wzyy.springbootinit.model.vo.LoginMessageV0;

import java.util.List;

/**
 * @description 登录信息service
 * @author: Wangzhaoyi
 * @create: 2023-12-29 16:07
 **/
public interface LoginMessageService {


    public List<LoginMessageV0> getLoginMessageList(LoginMessageQueryRequest request);
}
