package com.itheima.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Emma_Lyy
 * @create 2022-07-03 21:05
 */

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
