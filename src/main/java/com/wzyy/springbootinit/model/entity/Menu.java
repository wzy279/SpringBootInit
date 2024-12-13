package com.wzyy.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 权限表
 */
@Schema(description="权限表")
@Data
@TableName(value = "menu")
public class Menu implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    @Schema(description="id")
    private Long id;

    /**
     * 权限名称
     */
    @TableField(value = "menu_name")
    @Schema(description="权限名称")
    @NotBlank(message = "权限名称不能为空")
    private String menuName;

    /**
     * 权限编码
     */
    @TableField(value = "perms")
    @Schema(description="权限编码")
    @NotBlank(message = "权限编码不能为空")
    private String perms;

    /**
     * 父权限id
     */
    @TableField(value = "parent_id")
    @Schema(description="父权限id")
    @NotNull(message = "父权限不能为空")
    private Long parentId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @Schema(description="创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @Schema(description="更新时间")
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableField(value = "is_delete")
    @Schema(description="是否删除")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}