package com.mars.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mars.framework.shiro.user.ShiroLoginUser;
import com.mars.framework.shiro.user.ShiroUserDetail;
import com.mars.system.dao.UserMapper;
import com.mars.system.dao.UserPermissionMapper;
import com.mars.system.entity.UserEntity;
import com.mars.system.entity.UserPermissionEntity;
import com.mars.system.model.LoginUserInfo;
import com.mars.system.model.UserInfo;
import com.mars.system.service.UserService;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;
    private UserPermissionMapper userPermissionMapper;

    public UserServiceImpl(UserMapper userMapper, UserPermissionMapper userPermissionMapper) {
        this.userMapper = userMapper;
        this.userPermissionMapper = userPermissionMapper;
    }

    @Override
    public UserEntity queryUser(String userId) {

        UserInfo userInfo = userMapper.queryUserInfo(userId);

        return userInfo;
    }

    @Override
    public void updateUser(UserEntity userEntity) {
        userMapper.updateById(userEntity);
    }

    @Override
    public ShiroUserDetail login(ShiroLoginUser loginUser) throws AuthenticationException {

        String username = loginUser.getUsername();

        UserEntity userEntity = userMapper.selectOne(new QueryWrapper<UserEntity>()
                .lambda()
                .eq(UserEntity::getUsername, username));

        if (userEntity == null) {
            throw new AuthenticationException("未找到此用户");
        }

        if (!userEntity.getPassword().equals(loginUser.getPassword())) {
            throw new AuthenticationException("用户名或密码错误");
        }

        List<UserPermissionEntity> userPermissionEntities = userPermissionMapper.selectList(new QueryWrapper<UserPermissionEntity>()
                .lambda()
                .eq(UserPermissionEntity::getUserId, userEntity.getId()));

        LoginUserInfo userDetail = new LoginUserInfo();
        userDetail.setUsername(username);
        userDetail.setId(userEntity.getId());

        userDetail.setPermissions(userPermissionEntities.stream().map(UserPermissionEntity::getPermission).collect(Collectors.toList()));

        return userDetail;
    }
}
