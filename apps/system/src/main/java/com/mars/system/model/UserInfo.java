package com.mars.system.model;

import com.mars.system.entity.UserEntity;
import lombok.Data;

@Data
public class UserInfo extends UserEntity {

    private String orgName;

}
