package com.framework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties
public class Test {

    public static void main(String[] args) {

        ConfigurableApplicationContext applicationContext = SpringApplication.run(Test.class, args);

        System.out.println(applicationContext);

    }

}
