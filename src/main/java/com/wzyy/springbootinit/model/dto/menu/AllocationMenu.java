package com.wzyy.springbootinit.model.dto.menu;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 分配权限
 */
@Data
public class AllocationMenu {

    /**
     * 用户id
     */
    @NotEmpty(message = "用户不能为空")
    private List<Long> userId;
    /**
     * 权限id
     */
    @NotEmpty(message = "权限列表不能为空")
    private List<Long> menuId;
}
