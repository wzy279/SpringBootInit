package com.wzyy.springbootinit.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @description
 * @author: Wangzhaoyi
 * @create: 2023-12-27 14:00
 **/
@Data
public class UserChangePassRequest implements Serializable {

    /**
     * 用户邮箱
     */
    private String userEmail;
    /**
     * 用户密码
     */
    private String userPassword;
    /**
     * 确认密码
     */
    private String checkPassword;
    /**
     * 验证码
     */
    private String Captcha;

}
