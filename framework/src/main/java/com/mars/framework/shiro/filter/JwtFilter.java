package com.mars.framework.shiro.filter;

import com.mars.framework.shiro.token.JwtToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtFilter extends BaseShiroFilter {

    private static final String TOKEN_HEADER = "Authorization";

    /*
     * 1. 返回true，shiro 就直接允许访问url
     * 2. 返回false，shiro 才会根据 onAccessDenied 的方法的返回值决定是否允许访问url
     * */
    @Override
    protected boolean accessAllowed(ServletRequest request, ServletResponse response) throws Exception {

        //
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest servletRequest = (HttpServletRequest) request;

        String token = servletRequest.getHeader(TOKEN_HEADER);

        // 未携带 token 时，交给 shiro 判断 是否为放开权限接口（游客可访问接口）
        // 如果 当前接口需要权限访问，而又未携带 token ，shiro 会抛出 AuthorizationException，需处理此异常
        // 如果不返回 true ，则所有的 url 都需要携带 token 进行权限校验
        if (token == null || "".equals(token)){
            return true;
        }

        // 验证 token 的有效性
        JwtToken jwtToken = new JwtToken(token);
        try {
            // 权限认证
            getSubject(request, response).login(jwtToken);
        }catch (Exception e){
            e.printStackTrace();
            throw new AuthorizationException();
        }
        return true;
    }

}
