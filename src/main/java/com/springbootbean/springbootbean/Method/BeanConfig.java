package com.springbootbean.springbootbean.Method;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean(name="testDemo")
    public Demo generateDemo(){
        Demo demo = new Demo();
        demo.setId(1234);
        demo.setName("liu");
        return demo;
    }
}
