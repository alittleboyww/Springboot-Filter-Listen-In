package com.springbootbean.springbootbean.servletDemo;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class MyInterceptor implements HandlerInterceptor{
    //在请求处理前进行调用（Controller方法调用之前）
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,Object object)throws Exception{
        System.out.println("preHandle被调用");
        Map map = (Map) httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        System.out.println(map.get("name") + " preHandle");
        System.out.println(httpServletRequest.getParameter("username") + " preHandle");
        if(map.get("name").equals("zhangsan")){
            System.out.println(map.get("name"));
            return true;      //如果false ，停止流程，api被拦截
        }else{
            PrintWriter printWriter = httpServletResponse.getWriter();
            printWriter.write("please login again");
            return false;
        }
    }
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, ModelAndView modelAndView) throws Exception{
        System.out.println("postHandle被调用");
    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,Object object,Exception exception) throws Exception{
        System.out.println("afterCompletion被调用");
    }
}
