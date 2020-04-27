package com.mars.framework.datasource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@MapperScan("com.mars.**.dao")
@Configuration
public class MybatisConfig {
}
