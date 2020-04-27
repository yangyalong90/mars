package com.mars.framework.jwt;

public interface JwtOperator {

    String token(TokenUser user);

    boolean verify(String token);

    TokenUser parse(String token);

}
