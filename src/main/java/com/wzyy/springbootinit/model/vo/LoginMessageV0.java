package com.wzyy.springbootinit.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wzyy.springbootinit.model.entity.LoginMessage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
* @description
*
* @author: Wangzhaoyi
* @create: 2023-12-29 14:07
**/

/**
    * 用户登录信息表
    */
@ApiModel(description="用户登录信息表返回表")
@Data
public class LoginMessageV0 implements Serializable {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 登录方式
     */
    private String loginWay;

    /**
     * 设备名称
     */
    private String loginDeviceName;

    /**
     * 设备系统
     */
    private String loginDeviceSystem;

    /**
     * 登录时间
     */
    private Date loginTime;





    private static final long serialVersionUID = 1L;
}
