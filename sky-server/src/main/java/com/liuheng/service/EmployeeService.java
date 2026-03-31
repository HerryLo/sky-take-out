package com.liuheng.service;

import com.liuheng.dto.EmployeeDTO;
import com.liuheng.dto.EmployeeLoginDTO;
import com.liuheng.dto.EmployeePageQueryDTO;
import com.liuheng.dto.EmployeeStatusDTO;
import com.liuheng.entity.Employee;
import com.liuheng.result.PageResult;

public interface EmployeeService {
    Employee login(EmployeeLoginDTO employeeLoginDTO);
    boolean save(EmployeeDTO employee);
    PageResult search(EmployeePageQueryDTO employeePageQueryDTO);
    boolean update(EmployeeDTO employee);
    boolean updateStatus(EmployeeStatusDTO employeeStatusDTO);
}