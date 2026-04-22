package com.liuheng.mapper;

import com.liuheng.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE openid = #{openid}")
    User getByOpenid(String openid);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User getById(Long id);

    int save(User user);
}
