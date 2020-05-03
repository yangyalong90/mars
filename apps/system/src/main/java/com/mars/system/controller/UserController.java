package com.mars.system.controller;

import com.mars.common.model.ApiResult;
import com.mars.system.entity.UserEntity;
import com.mars.system.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sys/user")
public class UserController {

    private UserService userService;
    private KafkaTemplate<String, String> kafkaTemplate;

    public UserController(UserService userService, KafkaTemplate<String, String> kafkaTemplate) {
        this.userService = userService;
        this.kafkaTemplate = kafkaTemplate;
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

    @GetMapping("/other/kafka")
    @ResponseBody
    public ApiResult kafka(){
        kafkaTemplate.send("TOPIC_INSERT", "1");
        kafkaTemplate.send("TOPIC_UPDATE", "1");
        return ApiResult.success();
    }


}
