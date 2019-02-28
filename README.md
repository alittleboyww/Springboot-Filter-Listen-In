### SpringBoot过滤器、监听器、拦截器的理解

1. 定义自己的过滤器
```
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

```
2. 定义自己的监听器
```
package com.springbootbean.springbootbean.servletDemo;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class MyHttpSessionListener implements HttpSessionListener{
    public static int online = 0;
    @Override
    public void sessionCreated(HttpSessionEvent sessionEvent){
        System.out.println("创建session");
        online++;
    }
    @Override
    public void sessionDestroyed(HttpSessionEvent sessionEvent){
        System.out.println("销毁session");
    }
}

```
3. 定义自己的拦截器
```
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

```
4. 添加java配置，也即注册上面定义的类
```
package com.springbootbean.springbootbean.servletDemo;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebConfig implements WebMvcConfigurer{
    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/zxc/foo").setViewName("foo");
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new MyInterceptor()).addPathPatterns("/asd/**");
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    @Bean
    public FilterRegistrationBean filterRegist(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new MyFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        System.out.println("filter");
        return filterRegistrationBean;
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    @Bean
    public ServletListenerRegistrationBean listenerRegis(){
        ServletListenerRegistrationBean srb = new ServletListenerRegistrationBean();
        srb.setListener(new MyHttpSessionListener());
        System.out.println("listener");
        return srb;
    }
}
```
5. 创建Controller，代码如下
```
package com.springbootbean.springbootbean.servletDemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;

@Controller
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${application.message:Hello World}")
    private String message;

    //用于测试拦截器
    @GetMapping("/asd/{name}")
    public String welcome(@PathVariable String name, Map<String,Object> model){
        model.put("time",new Date());
        model.put("message",this.message);
        return "welcome";
    }
    @RequestMapping("/login")
    @ResponseBody
    public Object foo(){
        logger.info("打印日志============");
        return "login";
    }
    //测试过滤器
    @RequestMapping("/index")
    @ResponseBody
    public Object index(HttpServletRequest request){
        HttpSession session = request.getSession(true);
        session.setAttribute("zxc","zxc");
        return "index";
    }
    //测试监听器
    @RequestMapping("online")
    @ResponseBody
    public Object online(){
        return "当前在线人数"+MyHttpSessionListener.online + "人";
    }

}

```

6. 定义自己的Servlet
```
package com.springbootbean.springbootbean.servletDemo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//测试的时候利用index 避免被过滤器过滤以便被正常访问
@WebServlet(urlPatterns = "/index/serv/*")
public class MyServlet2 extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        System.out.println("-------doGet2--------");
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
        System.out.println("--------doPost2---------");
        response.getWriter().print("<h1>Hello MyServlet Response return you this2</h1>");
    }

    @Override
    public void init() throws ServletException{
        super.init();
    }
}

```
7. 在访问的时候可能会出先执行多次过滤器的情况，原因可能在于发送了这个请求http://localhost:8888/favicon.ico，自己可以利用浏览器的开发者工具进行查看解决