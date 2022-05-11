package com.compass.ms.service;

import com.compass.ms.DTO.TokenDTO;
import com.compass.ms.entity.Instances;
import com.compass.ms.exceptions.EntityExceptionResponse;
import com.compass.ms.repository.UserRepository;
import com.compass.ms.security.AuthenticationService;
import com.compass.ms.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("User Service Test")
public class LoginTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TokenService tokenService;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    WebSecurityConfiguration webSecurityConfiguration;

    @MockBean
    AuthenticationService authenticationService;

    @Autowired
    UserService userService;

    @MockBean
    AuthenticationException authenticationException;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup( userRepository,
                authenticationManager,  tokenService, webSecurityConfiguration,
                authenticationService, userService, authenticationException ).build();
    }

    @Test
    @DisplayName("Deve gerar token no User Service")
    public void generationTokenInUserServiceTest(){
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(Instances.userInstance()));
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(tokenService.generate(any())).thenReturn(Instances.tokenDTO().getToken());
        TokenDTO tokenDTO = userService.login(Instances.loginForm());
        assertEquals(tokenDTO.getToken(), Instances.tokenDTO().getToken());
        assertEquals(tokenDTO.getType(), Instances.tokenDTO().getType());
    }


    @Test
    @DisplayName("Deve lançar ResponseStatusException ao tentar login com dados incorretos")
    public void shouldThrowResponseStatusExceptionWhenTryLogin(){
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(Instances.userInstance()));
        doThrow(authenticationException).when(authenticationManager).authenticate(any());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.login(Instances.loginForm()));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }


    @Test
    @DisplayName("Deve lançar EntityExceptionResponse ao tentar login com usuário inválido")
    public void shouldThrowEntityExceptionResponseWhenTryLoginWithInvalidUser(){
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        EntityExceptionResponse exception = assertThrows(EntityExceptionResponse.class,
                () -> userService.login(Instances.loginForm()));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Email não encontrado.", exception.getMessage());

    }



}
