package com.mars.framework.shiro.filter;

import com.mars.framework.shiro.exception.handler.ShiroExceptionHandler;

import javax.naming.AuthenticationException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DefaultShiroFilter extends BaseShiroFilter {

    private BaseShiroFilter delegate;

    private ShiroExceptionHandler shiroExceptionHandler;

    public DefaultShiroFilter(BaseShiroFilter delegate, ShiroExceptionHandler shiroExceptionHandler) {
        this.delegate = delegate;
        this.shiroExceptionHandler = shiroExceptionHandler;
    }

    @Override
    boolean accessAllowed(ServletRequest request, ServletResponse response) throws Exception {

        try {
            return delegate.accessAllowed(request, response);
        }catch (Exception e){
            shiroExceptionHandler.handle((HttpServletRequest) request, (HttpServletResponse) response, e);
            return false;
        }

    }

}
