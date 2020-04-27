package com.mars.framework.shiro.filter;

import com.alibaba.fastjson.JSONObject;
import com.mars.framework.jwt.JwtOperator;
import com.mars.framework.jwt.JwtOperatorBuilder;
import com.mars.framework.shiro.service.LoginService;
import com.mars.framework.shiro.user.ShiroLoginUser;
import com.mars.framework.shiro.user.ShiroUserDetail;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.naming.AuthenticationException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginFilter extends BaseShiroFilter {

    private LoginService loginService;

    public LoginFilter(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    protected boolean accessAllowed(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String username = servletRequest.getParameter("username");
        String password = servletRequest.getParameter("password");

        ShiroLoginUser loginUser = new ShiroLoginUser(username, password);
        ShiroUserDetail userDetail = loginService.login(loginUser);

        JwtOperator jwtOperator = JwtOperatorBuilder.build();
        String token = jwtOperator.token(userDetail);

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("user", userDetail);

        response.getWriter().write(JSONObject.toJSONString(map));

        return false;
    }


}
