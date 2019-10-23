package com.yhcd.coding.ssjwt.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author hukai
 */
@Component
public class JwtUtils {
    @Value("${security.jwt.secret}")
    private String secret;

    public String generateToken(String id) {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setId(id)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}
