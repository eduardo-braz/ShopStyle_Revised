package com.compass.ms.security;

import com.compass.ms.DTO.UserDTO;
import com.compass.ms.entity.Instances;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("Token Service Test")
public class TokenServiceTest {

    @InjectMocks
    TokenService tokenService;

    @MockBean
    ModelMapper modelMapper;

    @MockBean
    Authentication authentication;

    @BeforeEach
    public void setup(){
        ReflectionTestUtils.setField(tokenService, "apiName", "Shop Style");
        ReflectionTestUtils.setField(tokenService, "secret",
        "(aR]e)rh%m3M$NU9QmGHS|G^56K|ri((0#NkUuP8OdndyNtCU2WT8QRf,dff@NOd{c$M,^CZag,n{{9sM^SdMzRq^xTJ#xBFcN}");
        ReflectionTestUtils.setField(tokenService, "expiration", "3600000");
    }

    @Test
    @DisplayName("Deve gerar um token JWT")
    public void shouldHaveReturnTokenJwt(){
        UserDTO userDTO = Instances.userDtoInstance();
        userDTO.setId(28L);
        when(modelMapper.map(eq(authentication.getPrincipal()), eq(UserDTO.class))).thenReturn(userDTO);
        String token = assertDoesNotThrow(() -> tokenService.generate(authentication));
        assertNotNull(token);
    }


}
