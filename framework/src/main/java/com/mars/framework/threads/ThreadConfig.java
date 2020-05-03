package com.mars.framework.threads;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadConfig {

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
        return new ThreadPoolTaskExecutor();
    }

    @Bean
    public WeakRefHashLock weakRefHashLock(){
        return new WeakRefHashLock();
    }

}
