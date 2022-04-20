package com.compass.ms.security;

import com.compass.ms.DTO.UserDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Autowired
    ModelMapper modelMapper;

    @Value("${bff.jwt.expiration}")
    private String expiration;

    @Value("${bff.jwt.secret}")
    private String secret;

    @Value("${bff.api-name}")
    private String apiName;

    public String generate(Authentication authentication) {
        UserDTO user = modelMapper.map(authentication.getPrincipal(), UserDTO.class);
        Date issuedAt = new Date();
        Date expirationTime = new Date(issuedAt.getTime() + Long.parseLong(expiration));
        return Jwts.builder()
                .setIssuer(apiName)
                .setSubject(user.getId().toString())
                .setIssuedAt(issuedAt)
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS256, this.secret)
                .compact();
    }

}
