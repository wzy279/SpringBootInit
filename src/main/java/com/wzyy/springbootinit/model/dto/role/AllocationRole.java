package com.wzyy.springbootinit.model.dto.role;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 分配权限
 */
@Data
public class AllocationRole {

    /**
     * 用户id
     */
    @NotEmpty(message = "用户不能为空")
    private List<Long> userId;
    /**
     * 权限id
     */
    @NotEmpty(message = "角色列表不能为空")
    private List<Long> role;
}
