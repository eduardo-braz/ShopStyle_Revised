package com.compass.ms.security;

import com.compass.ms.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.compass.ms.entity.Instances.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DisplayName("User Service Test")
public class AuthenticationServiceTest {

    @TestConfiguration
    static class AuthenticationServiceTestConfiguration{
        @Bean
        public AuthenticationService authenticationService(){
            return new AuthenticationService();
        }
    }

    @MockBean
    UserRepository userRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Test
    @DisplayName("Deve retornar um UserDetails ao encontrar usuario com email cadastrado")
    public void shouldHaveReturnUserDetailsWhenLoadUserByEmail(){
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userInstance()));
        UserDetails userDetails = authenticationService.loadUserByUsername(userInstance().getEmail());
        assertNotNull(userDetails);
        assertEquals(userDetails, userInstance());
    }


    @Test
    @DisplayName("Deve lançar UsernameNotFoundException ao tentar logar com email inválido")
    public void shouldThrowUsernameNotFoundExceptionWhenTryLoadUserWithInvalidEmail(){
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
                authenticationService.loadUserByUsername(userInstance().getEmail()));
        assertEquals("Email e/ou password inválido(s).", exception.getMessage());
    }


}
