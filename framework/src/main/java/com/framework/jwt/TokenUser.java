package com.framework.jwt;

import java.io.Serializable;

public interface TokenUser extends Serializable {

    /**
     * 默认 30 分钟 超时
     */
    int DEFAULT_EXPIRATION_MINUTES = 30;

    String getUsername();

    String getPassword();

    default int getExpirationMinutes(){
        return DEFAULT_EXPIRATION_MINUTES;
    }

}
