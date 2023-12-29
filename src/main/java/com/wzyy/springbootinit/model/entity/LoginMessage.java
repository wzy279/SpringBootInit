package com.wzyy.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
* @description
*
* @author: Wangzhaoyi
* @create: 2023-12-29 14:07
**/

/**
    * 用户登录信息表
    */
@ApiModel(description="用户登录信息表")
@Data
@TableName(value = "login_message")
public class LoginMessage implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value="id")
    private Long id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value="用户id")
    private Long userId;

    /**
     * 登录方式
     */
    @TableField(value = "login_way")
    @ApiModelProperty(value="登录方式")
    private String loginWay;

    /**
     * 设备名称
     */
    @TableField(value = "login_device_name")
    @ApiModelProperty(value="设备名称")
    private String loginDeviceName;

    /**
     * 设备系统
     */
    @TableField(value = "login_device_system")
    @ApiModelProperty(value="设备系统")
    private String loginDeviceSystem;

    /**
     * 登录时间
     */
    @TableField(value = "login_time")
    @ApiModelProperty(value="登录时间")
    private Date loginTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value="创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value="更新时间")
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableField(value = "is_delete")
    @ApiModelProperty(value="是否删除")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}
