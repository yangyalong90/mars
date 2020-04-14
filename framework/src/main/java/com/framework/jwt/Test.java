package com.framework.jwt;

import io.jsonwebtoken.*;

import java.util.Date;

public class Test {

    static String hs256EncodedSecretKey = "yyl-4012";
    static SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;

    public static void main(String[] args) throws Exception {
//        Date date = new Date(System.currentTimeMillis() + 10000L);
//        String token = Jwts.builder()
//                .claim("name", "a")
//                .setExpiration(date)
//                .signWith(algorithm, hs256EncodedSecretKey)
//                .compact();
//
//        JwtParser jwtParser = Jwts.parser().setSigningKey(hs256EncodedSecretKey);
//
//        Jwt parse = jwtParser.parse(token);
//
//        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        TokenUser tokenUser = new TokenUser() {

            private String username;
            private String password;

            public void setUsername(String username) {
                this.username = username;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            @Override
            public String getUsername() {
                return "a";
            }

            @Override
            public String getPassword() {
                return "a";
            }
        };

        JwtOperator operator = JwtOperatorBuilder.build();
        String token1 = operator.token(tokenUser);
        boolean verify = operator.verify(token1);
        TokenUser tokenUser1 = operator.parse(token1);

        System.out.println(tokenUser1);

    }

}
