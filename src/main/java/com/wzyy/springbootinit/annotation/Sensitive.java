package com.wzyy.springbootinit.annotation;

import cn.hutool.core.util.DesensitizedUtil;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wzyy.springbootinit.aop.SensitiveInfoSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description 敏感信息加密
 * @author: Wangzhaoyi
 * @create: 2023-11-12 12:27
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveInfoSerialize.class)
public @interface Sensitive {
    DesensitizedUtil.DesensitizedType value();
}
