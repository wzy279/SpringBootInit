package com.wzyy.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户_角色关联表
 */
@Schema(description="用户_角色关联表")
@Data
@TableName(value = "user_role")
public class UserRole implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    @Schema(description="id")
    private Long id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    @Schema(description="用户id")
    private Long userId;

    /**
     * 角色id
     */
    @TableField(value = "role_id")
    @Schema(description="角色id")
    private Long roleId;

    /**
     * 备注
     */
    @TableField(value = "remark")
    @Schema(description="备注")
    private String remark;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @Schema(description="创建时间")
    private Date createTime;

    /**
     * 是否删除
     */
    @TableField(value = "is_delete")
    @Schema(description="是否删除")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}