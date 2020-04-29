package com.mars.system.service;

import com.mars.framework.shiro.service.LoginService;
import com.mars.system.entity.UserEntity;

public interface UserService extends LoginService {

    UserEntity queryUser(String userId);

    void updateUser(UserEntity userEntity);

}
