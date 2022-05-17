package com.compass.ms.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TokenService {

    @Value("${bff.jwt.expiration}")
    private String expiration;

    @Value("${bff.jwt.secret}")
    private String secret;

    @Value("${bff.api-name}")
    private String apiName;

    public boolean tokenIsValid(String token) {
        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getUserId(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
            return Long.parseLong(claims.getSubject());
        } catch (ExpiredJwtException exceptione) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Expired Token");
        }
    }


}
