package com.wzyy.springbootinit.model.dto.loginMessage;

import com.wzyy.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @description
 * @author: Wangzhaoyi
 * @create: 2023-12-29 15:52
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginMessageQueryRequest  extends PageRequest implements Serializable {

    /**
     * 查询用户id
     */
    private Long userId;


    private static final long serialVersionUID = 1L;
}
