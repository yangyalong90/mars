package com.mars.framework.shiro.realm;

import com.mars.framework.jwt.JwtOperator;
import com.mars.framework.jwt.JwtOperatorBuilder;
import com.mars.framework.shiro.token.JwtToken;
import com.mars.framework.shiro.user.ShiroUserDetail;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.List;
import java.util.stream.Collectors;

public class CustomRealm extends AuthorizingRealm {

    private JwtOperator operator = JwtOperatorBuilder.build();

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 这里执行鉴权过程
     * 对于配置了需要鉴权url，执行此方法
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        String ticket = (String) principals.getPrimaryPrincipal();

        ShiroUserDetail userDetail = (ShiroUserDetail) operator.parse(ticket);

        List<String> permissions = userDetail.getPermissions();

        // 获取当前用户信息
        // 判断权限
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permissions == null ? null : permissions.stream().collect(Collectors.toSet()));

        return info;
    }

    /**
     * 获取认证信息
     * 如判断用户存不存在
     * 用户密码是否正确等
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        return new SimpleAuthenticationInfo(token.getPrincipal(), token.getCredentials(), "realmName");
    }
}