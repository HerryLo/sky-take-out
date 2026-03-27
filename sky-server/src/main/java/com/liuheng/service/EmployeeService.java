package com.liuheng.service;

import com.liuheng.dto.EmployeeLoginDTO;
import com.liuheng.entity.Employee;
import com.liuheng.vo.UserLoginVO;

public interface EmployeeService {
    public Employee login(EmployeeLoginDTO employeeLoginDTO);
}
