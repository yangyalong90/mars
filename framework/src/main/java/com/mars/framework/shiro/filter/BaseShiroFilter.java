package com.mars.framework.shiro.filter;

import com.mars.framework.shiro.exception.handler.ShiroExceptionHandler;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public abstract class BaseShiroFilter extends AccessControlFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {

        response.setContentType("application/json;charset=UTF-8");

        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return accessAllowed(request, response);
    }

    abstract boolean accessAllowed(ServletRequest request, ServletResponse response) throws Exception;

}
