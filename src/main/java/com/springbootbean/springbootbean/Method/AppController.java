package com.springbootbean.springbootbean.Method;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/application")
public class AppController {
    @RequestMapping("/test1")
    public Object testSpringUtil(){
        return SpringUtil.getBean("testDemo");
    }
}
