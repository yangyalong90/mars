package com.framework.shiro.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.framework.jwt.TokenUser;

import java.util.List;

public class ShiroUserDetail implements TokenUser {

    private String username;

    private String password;

    private List<String> permissions;

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
