package com.it.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.it.example.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
