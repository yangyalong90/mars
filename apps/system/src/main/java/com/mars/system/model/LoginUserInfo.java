package com.mars.system.model;

import com.mars.framework.shiro.user.ShiroUserDetail;
import lombok.Data;

@Data
public class LoginUserInfo extends ShiroUserDetail {

    private String id;

}
