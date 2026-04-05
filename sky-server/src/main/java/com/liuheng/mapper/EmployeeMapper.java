package com.liuheng.mapper;

import com.liuheng.annotation.AutoFill;
import com.liuheng.entity.Employee;
import com.liuheng.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmployeeMapper {
    /**
     * 查询员工
     * @param username
     * @return
     */
    Employee getByUsername(String username);


    /**
     * 查询员工
     * @param id
     * @return
     */
    Employee getById(Long id);

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @AutoFill(value = OperationType.INSERT)
    int save(Employee employee);

    /**
     * 分页查询员工
     * @param name
     * @param status
     * @return
     */
    List<Employee> list(String name, Integer status, Long id);

    /**
     * 修改员工
     * @param employee
     * @return
     */
    @AutoFill(value = OperationType.UPDATE)
    int update(Employee employee);

    /**
     * 启用禁用员工
     * @param status
     * @param id
     * @return
     */
    int updateStatus(Integer status, Long id);
}
