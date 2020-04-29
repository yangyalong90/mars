package com.mars.framework.datasource.cache;

import org.apache.ibatis.annotations.CacheNamespaceRef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheRelations {
    // from中mapper class对应的缓存更新时，需要更新当前注解标注mapper的缓存
    Class<?>[] from() default {};
    // 当前注解标注mapper的缓存更新时，需要更新to中mapper class对应的缓存
    Class<?>[] to() default {};
}
