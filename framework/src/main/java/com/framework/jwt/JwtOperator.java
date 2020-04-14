package com.framework.jwt;

public interface JwtOperator {

    String token(TokenUser user);

    boolean verify(String token);

    TokenUser parse(String token) throws TokenExpiredException;

}
