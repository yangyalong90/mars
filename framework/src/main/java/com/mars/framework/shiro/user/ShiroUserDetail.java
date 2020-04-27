package com.mars.framework.shiro.user;

import com.mars.framework.jwt.TokenUser;
import lombok.Data;

import java.util.List;

@Data
public class ShiroUserDetail implements TokenUser {

    private String username;

    private List<String> permissions;

}
