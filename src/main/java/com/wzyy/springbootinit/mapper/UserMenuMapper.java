package com.wzyy.springbootinit.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wzyy.springbootinit.model.entity.UserMenu;

public interface UserMenuMapper extends BaseMapper<UserMenu> {
    List<String> selectPermsByUserId(@Param("userId")Long userId);


}