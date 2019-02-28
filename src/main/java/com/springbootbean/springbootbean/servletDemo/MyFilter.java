package com.springbootbean.springbootbean.servletDemo;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig config) throws ServletException{
        System.out.println("过滤器初始化");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException,ServletException{
        System.out.println(request.getParameter("name") + " doFilter");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper((HttpServletResponse) response);
        if(httpServletRequest.getRequestURI().indexOf("/index") != -1||
                httpServletRequest.getRequestURI().indexOf("/asd") != -1||
                httpServletRequest.getRequestURI().indexOf("/online") != -1 ||
                httpServletRequest.getRequestURI().indexOf("/login") != -1){
            chain.doFilter(request,response);
        }else{
            wrapper.sendRedirect("/login");
        }
    }

    @Override
    public void destroy(){
        System.out.println("过滤器销毁");
    }
}
