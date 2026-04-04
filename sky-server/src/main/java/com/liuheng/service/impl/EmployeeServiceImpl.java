package com.liuheng.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.liuheng.constant.*;
import com.liuheng.context.BaseContext;
import com.liuheng.dto.*;
import com.liuheng.entity.Employee;
import com.liuheng.exception.*;
import com.liuheng.exception.IllegalArgumentException;
import com.liuheng.mapper.EmployeeMapper;
import com.liuheng.result.PageResult;
import com.liuheng.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    EmployeeMapper employeeMapper;

    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        Employee employee = employeeMapper.getByUsername(username);

        if(employee == null ) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    @Override
    public boolean save(EmployeeDTO employeeVo) {
        Employee emp = employeeMapper.getByUsername(employeeVo.getUsername());

        if(emp != null ) {
            //账号已存在
            throw new AccountNotFoundException(MessageConstant.ALREADY_EXISTS);
        }

        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeVo, employee);

        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        employee.setStatus(StatusConstant.ENABLE);
//        employee.setCreateTime(LocalDateTime.now());
        // TODO 更新人和创建人 是通过 ThreadLocal 进行保存id
//        employee.setUpdateUser(BaseContext.getCurrentId());
//        employee.setCreateUser(BaseContext.getCurrentId());

        return employeeMapper.save(employee) > 0;
    }

    @Override
    public PageResult search(EmployeePageQueryDTO employeePageQueryDTO) {
        // 1. 开启分页
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        // 2. 执行查询（紧跟着的查询会被分页）
        String name = employeePageQueryDTO.getName();
        Integer status = employeePageQueryDTO.getStatus();
        Long id = employeePageQueryDTO.getId();
        List<Employee> list = employeeMapper.list(name, status, id);
        Page<Employee> p = (Page<Employee>) list;

        return new PageResult(p.getTotal(), p.getResult());
    }

    @Override
    public boolean update(EmployeeDTO employeeVo) {
        Employee currentEmp = employeeMapper.getById(employeeVo.getId());
        if(currentEmp == null) {
            //员工不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        Employee emp = employeeMapper.getByUsername(employeeVo.getUsername());
        if(emp != null ) {
            //员工用户名已存在
            throw new AccountNotFoundException("员工用户名: username" + MessageConstant.ALREADY_EXISTS);
        }

        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeVo, employee);
//        employee.setUpdateUser(BaseContext.getCurrentId());
//        employee.setUpdateTime(LocalDateTime.now());

        return employeeMapper.update(employee) > 0;
    }

    @Override
    public boolean updateStatus(EmployeeStatusDTO employeeStatusDTO) {
        Employee currentEmp = employeeMapper.getById(employeeStatusDTO.getId());
        if(currentEmp == null) {
            //员工不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        return employeeMapper.updateStatus(employeeStatusDTO.getStatus(), employeeStatusDTO.getId()) > 0;
    }
}
