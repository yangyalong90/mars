package com.mars.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("user_permission")
@Data
public class UserPermissionEntity {

    private String id;
    private String userId;
    private String permission;

}
