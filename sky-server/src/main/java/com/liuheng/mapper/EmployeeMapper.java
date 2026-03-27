package com.liuheng.mapper;

import com.liuheng.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {
    /**
     * 查询用户
     * @param username
     * @return
     */
    Employee getByUsername(String username);
}
