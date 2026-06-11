package com.example.bankcards.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JWTTokenprovider {
    private static final String JWT_SECRET = "SecretKeyForJwtSigningMustBeVeryLongAndSecure1234567890!";
    private static final long JWT_EXPIRATION_MS = 86400000;

    private final Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());


    public String generateToken(Authentication authentication) {
        com.example.bankcards.security.UserPrincipal userPrincipal = (com.example.bankcards.security.UserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_MS);

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }


    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {

        }
        return false;
    }

}
