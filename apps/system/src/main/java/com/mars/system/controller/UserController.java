package com.mars.system.controller;

import com.mars.common.model.ApiResult;
import com.mars.system.entity.UserEntity;
import com.mars.system.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sys/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequiresPermissions("sys:user:query")
    @GetMapping("/{userId}")
    public ApiResult queryUser(@PathVariable("userId") String userId){
        return ApiResult.success(userService.queryUser(userId));
    }

    @RequiresPermissions("sys:user:update")
    @PutMapping("/{userId}")
    public ApiResult updateUser(@PathVariable("userId") String userId,@RequestBody UserEntity userEntity){
        userEntity.setId(userId);
        userService.updateUser(userEntity);
        return ApiResult.success();
    }

}
