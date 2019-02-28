package com.springbootbean.springbootbean.servletDemo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
