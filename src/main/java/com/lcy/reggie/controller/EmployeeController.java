package com.lcy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcy.reggie.common.R;
import com.lcy.reggie.pojo.Employee;
import com.lcy.reggie.service.EmployeeService;
import com.lcy.reggie.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @eo.api-type http
 * @eo.groupName 默认分组
 * @eo.path /employee
 */

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private EmployeeService employeeService;

/*    *//**
     * 员工登录
     * @param request
     * @param employee
     * @return
     * @eo.name 员工登录
     * @eo.url /login
     * @eo.method post
     * @eo.request-type json
     *//*
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){

        //1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3、如果没有查询到则返回登录失败结果
        if(emp == null){
            return R.error("登录失败");
        }

        //4、密码比对，如果不一致则返回登录失败结果
        if(!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }

        //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if(emp.getStatus() == 0){
            return R.error("账号已禁用");
        }

        //6、登录成功，将员工id存入Session并返回登录成功结果

        //将员工id设置为jwt荷载，返回给前端
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    *//**
     * 员工退出
     * @param request
     * @return
     * @eo.name 员工退出
     * @eo.url /logout
     * @eo.method post
     * @eo.request-type formdata
     *//*
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理Session中保存的当前登录员工的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }*/

    /**
     * @eo.name insert
     * @eo.url /
     * @eo.method post
     * @eo.request-type json
     * @param httpServletRequest
     * @param employee
     * @return R
     */
    @PostMapping
    public R<String> insert(HttpServletRequest httpServletRequest,@RequestBody Employee employee){
        log.info("新增员工: {}",employee);
        //设置初始密码,进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //设置创建时间当前系统时间
//        employee.setCreateTime(LocalDateTime.now());
        //更新时间
//        employee.setUpdateTime(LocalDateTime.now());
        //获取当前用户的id
        Long empId = (Long) httpServletRequest.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
        employeeService.save(employee);
        return R.success("新增成功");
    }
    @GetMapping("/page")
    public R<Page<Employee>> page(int page, int pageSize, String name){
        //构造分页插件
        Page<Employee> pageInfo =new Page<>(page,pageSize);
        //构造条件控制器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        //queryWrapper.like(StringUtils.isEmpty(name),Employee::getName,name);isEmpty被禁用
        //使用了String的trim()方法来去除字符串首尾空格，然后判断是否为空。如果不为空，则添加过滤条件。
        // 如果为空，则不添加过滤条件
        if(name != null && !name.trim().isEmpty()){
            queryWrapper.like(Employee::getName,name);
        }
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getCreateTime);
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 修改账号状态
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> updateById(HttpServletRequest request,@RequestBody Employee employee){

        long id =Thread.currentThread().getId();
        log.info("线程id:{}",id);
        //解析jwt令牌

        String jwt = (String) request.getSession().getAttribute("employee");
        //解析jwt
        Claims claims = jwtUtils.parseJWT(jwt);
        Long empId = Long.valueOf(claims.get("empId").toString());
        employee.setUpdateUser(empId);
//        employee.setUpdateTime(LocalDateTime.now());
        employeeService.updateById(employee);
        return R.success("修改成功");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工");
        Employee employee = employeeService.getById(id);
        return R.success(employee);
    }
}
