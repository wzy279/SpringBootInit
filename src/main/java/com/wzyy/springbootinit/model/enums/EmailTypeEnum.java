package com.wzyy.springbootinit.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色枚举
 *
 */
public enum EmailTypeEnum {

    findEmail("找回密码", "findEmailMessage","0"),
    Register("注册账号","RegisterMessage","1"),
    ReminderLogin("提醒登录", "ReminderLoginMessage","2");


    private final String text;

    private final String value;

    private final String encode;

    EmailTypeEnum(String text, String value, String encode) {
        this.text = text;
        this.value = value;
        this.encode = encode;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param encode
     * @return
     */
    public static EmailTypeEnum getEnumByValue(String encode) {
        if (ObjectUtils.isEmpty(encode)) {
            return null;
        }
        for (EmailTypeEnum anEnum : EmailTypeEnum.values()) {
            if (anEnum.encode.equals(encode)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }


    public String getEncode(){
        return encode;
    }
}
