package com.mars.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("user")
@Data
public class UserEntity {

    private String id;
    private String username;
    private String password;

}
