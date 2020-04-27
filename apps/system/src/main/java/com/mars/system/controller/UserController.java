package com.mars.system.controller;

import com.mars.common.model.ApiResult;
import com.mars.system.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequiresPermissions("sys:user:query")
    @GetMapping("/{username}")
    public ApiResult queryUser(@PathVariable("username") String username){
        return ApiResult.success(userService.queryUser(username));
    }

    @RequiresPermissions("sys:user:query1")
    @GetMapping("/1/{username}")
    public ApiResult queryUser1(@PathVariable("username") String username){
        return ApiResult.success(userService.queryUser(username));
    }

}
