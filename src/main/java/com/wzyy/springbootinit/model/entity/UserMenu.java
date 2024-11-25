package com.wzyy.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Data;

/**
 * 用户_权限对应表
 */
@Schema(description="用户_权限对应表")
@Data
@TableName(value = "user_menu")
public class UserMenu implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description="id")
    private Long id;

    /**
     * 权限id
     */
    @TableField(value = "menu_id")
    @Schema(description="权限id")
    private Long menuId;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    @Schema(description="用户id")
    private Long userId;

    /**
     * 是否删除
     */
    @TableField(value = "is_delete")
    @Schema(description="是否删除")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}