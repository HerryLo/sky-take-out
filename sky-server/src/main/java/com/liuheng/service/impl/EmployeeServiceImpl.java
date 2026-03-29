package com.liuheng.service.impl;

import com.liuheng.constant.MessageConstant;
import com.liuheng.constant.PasswordConstant;
import com.liuheng.constant.StatusConstant;
import com.liuheng.context.BaseContext;
import com.liuheng.dto.EmployeeLoginDTO;
import com.liuheng.entity.Employee;
import com.liuheng.exception.AccountLockedException;
import com.liuheng.exception.AccountNotFoundException;
import com.liuheng.exception.PasswordErrorException;
import com.liuheng.mapper.EmployeeMapper;
import com.liuheng.service.EmployeeService;
import com.liuheng.vo.EmployeeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

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
    public void save(EmployeeVO employeeVo) {
        Employee employee = new Employee();

        BeanUtils.copyProperties(employeeVo, employee);

        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        employee.setStatus(StatusConstant.ENABLE);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId());
        employee.setCreateUser(BaseContext.getCurrentId());

        employeeMapper.insert(employee);
    }
}
