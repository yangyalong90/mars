package com.mars.framework.shiro.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

@Getter
public class ShiroLoginUser {

    private String username;

    private String password;

    public ShiroLoginUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    void setUsername(String username){
        this.username = username;
    }

    void setPassword(String password){
        this.password = password;
    }

}
