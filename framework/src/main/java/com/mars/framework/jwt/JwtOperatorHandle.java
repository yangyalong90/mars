package com.mars.framework.jwt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Base64;
import java.util.Date;

class JwtOperatorHandle implements JwtOperator {

    private static final String ENCODE_SECRET_KEY = "yyl-4012-secret-key";
    private static final String USER_VALUE_KEY = "user-value-key";
    private static final String USER_CLASS_KEY = "user-class-key";
    private SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;

    private byte[] base64EncodeSecretKey;

    public JwtOperatorHandle() {
        base64EncodeSecretKey = Base64.getEncoder().encode(ENCODE_SECRET_KEY.getBytes());
    }

    public String token(TokenUser user) {

        Date now = new Date();

        String token = Jwts.builder()
                .claim(USER_VALUE_KEY, JSON.toJSONString(user))
                .claim(USER_CLASS_KEY, user.getClass())
                .setIssuedAt(now)
                .setExpiration(new Date(
                                now.getTime() + user.getExpirationMinutes() * 60 * 1000
                        )
                )
                .signWith(algorithm, base64EncodeSecretKey)
                .compact();

        return token;
    }

    public boolean verify(String token) {

        try {
            Jwts.parser().setSigningKey(base64EncodeSecretKey).parse(token);
        } catch (ExpiredJwtException e) {
            return false;
        }

        return true;
    }

    public TokenUser parse(String token) {

        Claims claims;
        try {
            claims = (Claims) Jwts.parser()
                    .setSigningKey(base64EncodeSecretKey)
                    .parse(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException();
        }

        String userJson = claims.get(USER_VALUE_KEY, String.class);
        String clazzString = claims.get(USER_CLASS_KEY, String.class);

        Class<TokenUser> clazz;
        try {
            clazz = (Class) Class.forName(clazzString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        TokenUser tokenUser = JSONObject.parseObject(userJson, clazz);

        return tokenUser;
    }
}
