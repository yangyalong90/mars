package com.mars.framework.shiro.exception.handler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

public class DefaultExceptionResultParse implements ExceptionResultParse {
    @Override
    public String parse(String result) {
        return result;
    }
}
