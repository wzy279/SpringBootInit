package com.wzyy.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wzyy.springbootinit.model.entity.LoginMessage;
import org.apache.ibatis.annotations.Mapper;

/**
* @description
*
* @author: Wangzhaoyi
* @create: 2023-12-29 14:07
**/

@Mapper
public interface LoginMessageMapper extends BaseMapper<LoginMessage> {
}
