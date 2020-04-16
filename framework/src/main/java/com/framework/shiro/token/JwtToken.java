package com.framework.shiro.token;

import org.apache.shiro.authc.AuthenticationToken;

public class JwtToken implements AuthenticationToken {

    /**
     * token jwt 生成
     */
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    /**
     * 返回类似用户名
     * 这里返回 token
     * @return
     */
    @Override
    public Object getPrincipal() {
        return token;
    }

    /**
     * 返回类似密码
     * 这里返回 token
     * @return
     */
    @Override
    public Object getCredentials() {
        return token;
    }
}
