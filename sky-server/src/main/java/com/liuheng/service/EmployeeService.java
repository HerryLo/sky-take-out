package com.liuheng.service;

import com.liuheng.dto.EmployeeLoginDTO;
import com.liuheng.entity.Employee;
import com.liuheng.vo.EmployeeVO;
import com.liuheng.vo.UserLoginVO;

public interface EmployeeService {
    Employee login(EmployeeLoginDTO employeeLoginDTO);
    boolean save(EmployeeVO employee);
}