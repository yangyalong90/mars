package com.framework.shiro.exception.handler;

import com.framework.jwt.TokenExpiredException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ShiroExceptionHandler {

    @ExceptionHandler(AuthorizationException.class)
    @ResponseBody
    public String notAuthorized(HttpServletResponse response){

        response.setStatus(401);
        return "你没有权限";

    }

    @ExceptionHandler(TokenExpiredException.class)
    @ResponseBody
    public String tokenExpired(HttpServletResponse response){

        response.setStatus(405);
        return "token 已失效，请重新登陆";

    }

}
