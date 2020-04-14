package com.framework.jwt;

import io.jsonwebtoken.*;

import java.util.Date;

public class Test {

    static String hs256EncodedSecretKey = "yyl-4012";
    static SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;

    public static void main(String[] args) {
        Date date = new Date(System.currentTimeMillis() + 10000L);
        String token = Jwts.builder()
                .claim("name", "a")
                .setExpiration(date)
                .signWith(algorithm, hs256EncodedSecretKey)
                .compact();

        JwtParser jwtParser = Jwts.parser().setSigningKey(hs256EncodedSecretKey);

        Jwt parse = jwtParser.parse(token);

        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        System.out.println(claims);

    }

}
