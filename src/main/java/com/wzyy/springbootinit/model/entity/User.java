package com.wzyy.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户
 *
 */
@TableName(value = "user")
@Data
public class User implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户账号
     */
    @TableField("user_account")
    private String userAccount;

    /**
     * 用户密码
     */
    @TableField("user_password")
    private String userPassword;

    /**
     * 用户昵称
     */
    @TableField("user_name")
    private String userName;

    /**
     * 用户头像
     */
    @TableField("user_avatar")
    private String userAvatar;

    /**
     * 用户简介
     */
    @TableField("user_profile")
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    @TableField("user_role")
    private String userRole;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 用户邮箱
     */
    @TableField("user_email")
    private String userEmail;

    /**
     * 是否删除
     */
    @TableLogic
    @TableField("is_delete")
    private Integer isDelete;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
