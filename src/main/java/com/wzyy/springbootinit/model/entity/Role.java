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
 * 角色信息表
 */
@Schema(description="角色信息表")
@Data
@TableName(value = "`role`")
public class Role implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    @Schema(description="id")
    private Long id;

    /**
     * 角色编码
     */
    @TableField(value = "role_code")
    @Schema(description="角色编码")
    private String roleCode;

    /**
     * 角色名称
     */
    @TableField(value = "role_name")
    @Schema(description="角色名称")
    private String roleName;

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