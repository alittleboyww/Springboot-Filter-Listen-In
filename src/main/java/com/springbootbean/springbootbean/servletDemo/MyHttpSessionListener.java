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
