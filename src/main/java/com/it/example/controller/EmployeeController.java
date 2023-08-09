package com.it.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.it.example.common.R;
import com.it.example.entity.Employee;
import com.it.example.service.EmployeeService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request  用于存储登陆成功的员工id
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        /**
         * 1、将页面提交的密码password进行md5加密处理
         * 2、根据页面提交的用户名username查询数据库
         * 3、如果没有查询到则返回登录失败结果
         * 4、密码比对，如果不一致则返回登录失败结果
         * 5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
         * 6、登录成功,将员工id存入Session并返回登录成功结果
         */
        //1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password=DigestUtils.md5DigestAsHex(password.getBytes());
        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());  //Employee::getUsername 创建一个Employee实例 并得到username
        Employee emp = employeeService.getOne(queryWrapper);

        // 3、如果没有查询到则返回登录失败结果
        if(emp == null){
            return R.error("登录失败");
        }
        //4、密码比对，如果不一致则返回登录失败结果
        if(!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }

        //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if(emp.getStatus()==0){
            return R.error("账号已禁用");
        }
        //6、登录成功,将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());

        return R.success(emp);
    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功！");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工，员工信息：{}",employee.toString());
        //设置初始密码，进行MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//        Long userId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(userId);
//        employee.setUpdateUser(userId);

        employeeService.save(employee);

        /**
         *         try {
         *             employeeService.save(employee);
         *         }catch (Exception e){
         *             R.error("新增员工失败")
         *         }
         */

        return R.success("新增员工成功");
    }

    /**
     * 员工信息分页查询
     * 1.页面发送Ajax请求，将分页查询参数(page,pagesize,name)提交到服务器
     * 2.服务器Controller接收页面提交的数据 并调用service查询数据
     * 3.service调用mapper操作数据库，查询数据
     * 4.controller将查询到的分页数据响应给页面
     * 5.页面接收到分页数据并通过elementUI的table组件展示到页面上
     */
    /**
     * 员工信息分页查询
     * @param page  这个是根据前端发送请求 的参数 来决定这三个参数的
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);

        //分页构造器
        Page pageInfo = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询  查询结果会自动封装给pageInfo
        employeeService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 根据id修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());

//        Long id = Thread.currentThread().getId();
//        log.info("线程id为{}",id);

//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateUser(empId );
//        employee.setUpdateTime(LocalDateTime.now());
        employeeService.updateById(employee);
        return R.success("员工信息修改成功 ！");
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息...");
        Employee employee=employeeService.getById(id);
        if(employee!=null){
            return R.success(employee);
        }
        return R.error("没有查询到员工信息");

    }

}
