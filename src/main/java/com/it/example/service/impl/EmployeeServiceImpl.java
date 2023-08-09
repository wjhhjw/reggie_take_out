package com.it.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.example.entity.Employee;
import com.it.example.mapper.EmployeeMapper;
import com.it.example.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
