package com.framework.shiro.filter;

import com.framework.shiro.token.JwtToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtFilter extends AccessControlFilter {

    private static final String TOKEN_HEADER = "Authorization";

    /*
     * 1. 返回true，shiro 就直接允许访问url
     * 2. 返回false，shiro 才会根据 onAccessDenied 的方法的返回值决定是否允许访问url
     * */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {

        // 这里直接返回 false ，让所有的有权限校验接口在 onAccessDenied 方法中处理
        return false;
    }

    /**
     * 返回结果为true表明登录通过
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        // todo
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest servletRequest = (HttpServletRequest) request;

        String token = servletRequest.getHeader(TOKEN_HEADER);

        if (token == null || "".equals(token)){
            return true;
        }

        // todo 验证 token 的有效性

        JwtToken jwtToken = new JwtToken(token);
        try {
            getSubject(request, response).login(jwtToken);
        }catch (Exception e){
            e.printStackTrace();
            httpResponse.setStatus(401);
            httpResponse.getWriter().write("access denied");
            return false;
        }
        return true;
    }
}
