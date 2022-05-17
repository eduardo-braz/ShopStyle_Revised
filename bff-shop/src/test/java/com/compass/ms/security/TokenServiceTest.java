package com.compass.ms.security;

import com.compass.ms.instances.Instances;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("Token Service Test")
public class TokenServiceTest {

    @InjectMocks
    TokenService tokenService;

    String secret = "(aR]e)rh%m3M$NU9QmGHS|G^56K|ri((0#NkUuP8OdndyNtCU2WT8QRf,dff@NOd{c$M,^CZag,n{{9sM^SdMzRq^xTJ#xBFcN}";
    String expiration = "3600000";
    String apiName = "Shop Style";

    @BeforeEach
    public void setup(){
        ReflectionTestUtils.setField(tokenService, "apiName", apiName);
        ReflectionTestUtils.setField(tokenService, "secret", secret);
        ReflectionTestUtils.setField(tokenService, "expiration", expiration);
    }

    private String generateToken() {
        Date issuedAt = new Date();
        Date expirationTime = new Date(issuedAt.getTime() + Long.parseLong(expiration));
        return Jwts.builder()
                .setIssuer(apiName)
                .setSubject(Instances.user().getId().toString())
                .setIssuedAt(issuedAt)
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    @Test
    public void shouldHaveReturnTrueWhenTokenIsValid(){
        String token = generateToken();
        assertTrue(tokenService.tokenIsValid(token));
    }

    @Test
    public void shouldHaveReturnFalseWhenTokenIsInvalid(){
        String token = "invalidToken$#%invalidToken$#%invalidToken$#%invalidToken$";
        assertFalse(tokenService.tokenIsValid(token));
    }

    @Test
    public void shouldHaveReturnLongWhenGetUserIdUsingToken(){
        String token = generateToken();
        Long userId = tokenService.getUserId(token);
        assertEquals(53L, userId);
    }

    @Test
    public void shouldThrowExceptionWhenTryGetUserIdWithExpiratedToken(){
        String token = Instances.tokenDTO().getToken();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            Long userId = tokenService.getUserId(token);
            assertNull(userId);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Expired Token", exception.getReason());
    }

}
