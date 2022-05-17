package com.compass.ms.security;

import com.compass.ms.clientFeign.CustomerClient;
import com.compass.ms.instances.Instances;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("Authentication Service Test")
public class AuthenticationServiceTest {

    @TestConfiguration
    public static class AuthenticationServiceTestConfiguration{
        @Bean
        public AuthenticationService init(){
            return new AuthenticationService();
        }

        @Bean
        public ModelMapper modelMapper(){
            return new ModelMapper();
        }
    }

    @MockBean
    CustomerClient customerClient;

    @Autowired
    AuthenticationService authenticationService;

    @Test
    public void shouldHaveReturnUserDetailsWhenLoadUserById(){
        when(customerClient.findById(53L)).thenReturn(Instances.userDTO());
        UserDetails userDetails = authenticationService.loadUserByUsername("53");
        assertNotNull(userDetails);
        assertEquals(Instances.login().getEmail(), userDetails.getUsername());
    }

    @Test
    public void shouldHaveThrowUsernameNotFoundExceptionWhenTryLoadUserWithInvalidId(){
        when(customerClient.findById(53L)).thenThrow(UsernameNotFoundException.class);
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            UserDetails userDetails = authenticationService.loadUserByUsername("53");
            assertNull(userDetails);
        });
        assertEquals("Email e/ou password inválido(s).", exception.getMessage());
    }

}
