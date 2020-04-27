package com.mars.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SystemApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SystemApplication.class, args);

        System.out.println("system 启动成功");

    }

}
