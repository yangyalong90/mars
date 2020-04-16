package com.framework.shiro.service;

import com.framework.shiro.user.ShiroLoginUser;
import com.framework.shiro.user.ShiroUserDetail;

public interface LoginService {

    ShiroUserDetail login(ShiroLoginUser loginUser);

}
