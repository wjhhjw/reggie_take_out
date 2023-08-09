package com.it.example.filter;

import com.alibaba.fastjson.JSON;
import com.it.example.common.BaseContext;
import com.it.example.common.R;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否已经完成登录
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
//    路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    /**
     * 过滤器具体的处理逻辑如下:1、获取本次请求的URI 2、判断本次请求是否需要处理’3、如果不需要处理，则直接放行 4、判断登录状态，如果已登录,则直接放行5、如果未登录则返回未登录结果
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1、获取本次请求的URI
        String requestURI=request.getRequestURI(); //  /backend/index.html
        log.info("本次请求路径："+requestURI);
        // 2、判断本次请求是否需要处理
        String[] urls = new String[]{
          "/employee/login" ,
          "/employee/logout",
          "/backend/**",
          "/front/**"
        }; //通配符不好匹配

        // 3、如果不需要处理，则直接放行
        boolean check = check(urls, requestURI);
        if (check){
            log.info("本次请求{}不需要处理",requestURI);
            //放行
            filterChain.doFilter(request,response);
            return;
        }

        // 4、判断登录状态，如果已登录,则直接放行
        if (request.getSession().getAttribute("employee")!=null){
            log.info("用户已登录，用户id为{}",request.getSession().getAttribute("employee"));

            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

//            Long id = Thread.currentThread().getId();
//            log.info("线程id为{}",id);

            //放行
            filterChain.doFilter(request,response);
            return;
        }
        log.info("用户未登录");
        // 5、如果未登录则返回未登录结果,则通过输出流的方式向前端页面输出响应结果
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }

    public boolean check(String[] urls,String requestURI){
        for(String url : urls){
            boolean match = PATH_MATCHER.match(url,requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
