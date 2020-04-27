package com.mars.common.exception;

import com.alibaba.fastjson.JSON;
import com.mars.common.model.ApiResult;
import com.mars.framework.shiro.exception.handler.ExceptionResultParse;
import org.springframework.stereotype.Component;

@Component
public class JsonExceptionResultParse implements ExceptionResultParse {
    @Override
    public String parse(String result) {

        return JSON.toJSONString(ApiResult.fail(result));
    }
}
