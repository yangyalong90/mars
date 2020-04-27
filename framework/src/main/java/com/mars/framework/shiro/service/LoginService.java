package com.mars.framework.shiro.service;

import com.mars.framework.shiro.user.ShiroLoginUser;
import com.mars.framework.shiro.user.ShiroUserDetail;

import javax.naming.AuthenticationException;

public interface LoginService {

    ShiroUserDetail login(ShiroLoginUser loginUser) throws AuthenticationException;

}
