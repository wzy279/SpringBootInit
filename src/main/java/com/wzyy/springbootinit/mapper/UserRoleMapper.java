package com.wzyy.springbootinit.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wzyy.springbootinit.model.entity.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole> {

    List<String> selectRoleCodeByUserId(@Param("userId")Long userId);


}