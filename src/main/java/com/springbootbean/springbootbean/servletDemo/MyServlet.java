package com.springbootbean.springbootbean.servletDemo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        System.out.println("-------doGet-------");
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
        System.out.println("-------------doPost-------");
        response.getWriter().print("<h1>Hello MyServlet Response return you this</h1>");
    }
    @Override
    public void init() throws ServletException{
        super.init();
    }
}
