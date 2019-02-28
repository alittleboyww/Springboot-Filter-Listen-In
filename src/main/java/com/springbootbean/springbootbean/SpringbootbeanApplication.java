package com.springbootbean.springbootbean;

import com.springbootbean.springbootbean.servletDemo.MyServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@ServletComponentScan
public class SpringbootbeanApplication {

	@Bean
	public ServletRegistrationBean MyServlet1(){
		return new ServletRegistrationBean(new MyServlet(),"/myserv/*");
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringbootbeanApplication.class, args);
	}
}
