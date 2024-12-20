package com.wzyy.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wzyy.springbootinit.model.dto.user.UserQueryRequest;
import com.wzyy.springbootinit.model.entity.User;
import com.wzyy.springbootinit.model.vo.LoginUserVO;
import com.wzyy.springbootinit.model.vo.UserVO;

import java.util.List;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 *
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @param userEmail
     * @param captcha
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword,String userEmail,String captcha);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLoginAccount(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取验证码
     *
     * @param email
     * @return
     */
    Boolean getCODE(String email,String encode) throws MessagingException;


    /**
     * 获取当前登录用户
     *
     * @return
     */
    User getLoginUser();

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request
     * @return
     */
    User getLoginUserPermitNull(HttpServletRequest request);


    /**
     * 用户注销
     *
     * @return
     */
    boolean userLogout();

    /**
     * 获取脱敏的已登录用户信息
     *
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param userList
     * @return
     */
    List<UserVO> getUserVO(List<User> userList);

    /**
     * 获取查询条件
     *
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

}
