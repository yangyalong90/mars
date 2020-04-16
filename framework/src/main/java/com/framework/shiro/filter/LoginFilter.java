package com.framework.shiro.filter;

import com.alibaba.fastjson.JSONObject;
import com.framework.jwt.JwtOperator;
import com.framework.jwt.JwtOperatorBuilder;
import com.framework.shiro.service.LoginService;
import com.framework.shiro.token.JwtToken;
import com.framework.shiro.user.ShiroLoginUser;
import com.framework.shiro.user.ShiroUserDetail;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class LoginFilter extends AccessControlFilter {

    private LoginService loginService;

    public LoginFilter(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        HttpServletRequest servletRequest = (HttpServletRequest) request;

        String username = servletRequest.getParameter("username");
        String password = servletRequest.getParameter("password");

        ShiroLoginUser loginUser = new ShiroLoginUser(username, password);
        ShiroUserDetail userDetail = loginService.login(loginUser);

        JwtOperator jwtOperator = JwtOperatorBuilder.build();
        String token = jwtOperator.token(userDetail);

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("user", userDetail);

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        response.getWriter().write(JSONObject.toJSONString(map));

        return false;
    }
}
