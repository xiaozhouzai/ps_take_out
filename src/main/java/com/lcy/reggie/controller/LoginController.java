package com.lcy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lcy.reggie.common.R;
import com.lcy.reggie.pojo.Employee;
import com.lcy.reggie.service.EmployeeService;
import com.lcy.reggie.utils.JwtRsaUtils;
import com.lcy.reggie.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
public class LoginController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     * @eo.name 员工登录
     * @eo.url /login
     * @eo.method post
     * @eo.request-type json
     */
    @PostMapping("/employee/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) throws Exception {

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
        Map<String,Object> emo=new HashMap<>();
        emo.put("empId",emp.getId());
        //6、登录成功，将员工id存入Session并返回登录成功结果
        String jwt = jwtUtils.generateJwt(emo);
//        String jwt = jwtRsaUtils.generateJwt(emo);
        //输出jwt
        request.getSession().setAttribute("employee",jwt);
        return R.success(emp);
    }

    /**
     * 员工退出
     * @param request
     * @return
     * @eo.name 员工退出
     * @eo.url /logout
     * @eo.method post
     * @eo.request-type formdata
     */
    @PostMapping("/employee/logout")
    public R<String> logout(HttpServletRequest request){
        //清理Session中保存的当前登录员工的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }
}
