package com.lcy.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.lcy.reggie.common.BaseContext;
import com.lcy.reggie.common.R;
import com.lcy.reggie.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;

/**
 * @eo.api-type http
 * @eo.groupName 默认分组
 * @eo.path
 */
@Slf4j
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    //路径匹配器
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();

    /**
     * @eo.name doFilter
     * @eo.url
     * @eo.method get
     * @eo.request-type formdata
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @return void
     */
    @SneakyThrows
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException{
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;
        //获取请求uri
        String requestURI = request.getRequestURI();
        log.info("拦截到请求:{}",request.getRequestURI());
        //定义不需要拦截的路径
        String[] urls=new String[]{
                "/backend/**",
                "/front/**",
                "/employee/login",
                "/employee/logout",
                "/user/login",
                "/user/sendMsg",
                "/common/**"
        };
        boolean check = check(urls, requestURI);

        //如果不需要处理直接放行
        if(check){
            filterChain.doFilter(request,response);
            return;
        }

        //pc端
        //判断登陆状态,令牌不为空
        if(request.getSession().getAttribute("employee") !=null){
            //获取令牌
            String jwt = (String) request.getSession().getAttribute("employee");
            //解析jwt
            JwtUtils jwtUtils=new JwtUtils();
            Claims claims = jwtUtils.parseJWT(jwt);

            Long empId = Long.valueOf(claims.get("empId").toString());
            log.info("用户已登录:{}",empId);
            //验证线程
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request,response);
            return;
        }
        //移动端
        if(request.getSession().getAttribute("user") !=null){


            Long userId = (Long) request.getSession().getAttribute("user");
            log.info("用户已登录:{}",userId);
            //验证线程
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request,response);
            return;
        }
        //如果未登录，返回登陆页面
        log.info("未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

    }


    /**
     * @eo.name check
     * @eo.url
     * @eo.method get
     * @eo.request-type formdata
     * @param urls
     * @param requestUri
     * @return boolean
     * 路径匹配，检查本次请求是否放行
     */
    public boolean check(String[] urls,String requestUri){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestUri);
            if (match){
                return true;
            }
        }
        return false;
    }
}
