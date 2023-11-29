package com.wzyy.springbootinit.annotation;

/**
 * @description
 * @author: Wangzhaoyi
 * @create: 2023-10-17 13:30
 **/

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
@Inherited
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE) //最高优先级
public @interface RequestLimit {
    /**
     *
     * 允许访问的次数，默认值MAX_VALUE
     */
    int count() default 10;

    /**
     *
     * 时间段，单位为毫秒，默认值一分钟
     */
    long time() default 60;
}
