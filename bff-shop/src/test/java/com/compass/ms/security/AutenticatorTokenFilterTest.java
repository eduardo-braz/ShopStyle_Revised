package com.compass.ms.security;

import com.compass.ms.DTOs.customer.User;
import com.compass.ms.clientFeign.CustomerClient;
import com.compass.ms.instances.Instances;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("Authentication Token Filter Test")
public class AutenticatorTokenFilterTest {

    @TestConfiguration
    public static class AutenticatorTokenFilterTestConfiguration{
        @Bean
        public ModelMapper modelMapper(){
            return new ModelMapper();
        }
    }

    @MockBean
    TokenService tokenService;

    @MockBean
    CustomerClient customerClient;

    @InjectMocks
    AutenticatorTokenFilter autenticatorTokenFilter;

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    FilterChain filterChain = mock(FilterChain.class);


    @Test
    public void shouldHaveAuthenticateCorrectlyAUser() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJTaG9wIFN0eWxlIiwic3ViIjoiMjgiLCJpYXQiOjE2NTA0NTg5MTcsImV4cCI6MTY1MDQ2MjUxN30.G9fcc7pY_w9MCEKPHuLBsAc55gaQ8XI1gnUt1iowJEM");
        when(tokenService.tokenIsValid(anyString())).thenReturn(true);
        when(tokenService.getUserId(anyString())).thenReturn(53L);
        when(customerClient.findById(53L)).thenReturn(Instances.userDTO());

        autenticatorTokenFilter.doFilterInternal(request, response, filterChain);

        verify(tokenService, times(1)).tokenIsValid(anyString());
        verify(tokenService, times(1)).getUserId(anyString());
        verify(customerClient, times(1)).findById(anyLong());
        verify(filterChain, times(1)).doFilter(request, response);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        assertEquals(Instances.login().getEmail(), user.getEmail());
    }

    @Test
    public void mustNotAuthenticateCorrectlyAUserBecauseTokenIsEmpty() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("");

        autenticatorTokenFilter.doFilterInternal(request, response, filterChain);

        verify(tokenService, times(0)).tokenIsValid(anyString());
        verify(tokenService, times(0)).getUserId(anyString());
        verify(customerClient, times(0)).findById(anyLong());
        verify(filterChain, times(1)).doFilter(request, response);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }

    @Test
    public void mustNotAuthenticateCorrectlyAUserBecauseTokenIsNotBearerType() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("OAuth  ");

        autenticatorTokenFilter.doFilterInternal(request, response, filterChain);

        verify(tokenService, times(0)).tokenIsValid(anyString());
        verify(tokenService, times(0)).getUserId(anyString());
        verify(customerClient, times(0)).findById(anyLong());
        verify(filterChain, times(1)).doFilter(request, response);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }

    @Test
    public void mustNotAuthenticateCorrectlyAUserBecauseTokenIsNull() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        autenticatorTokenFilter.doFilterInternal(request, response, filterChain);

        verify(tokenService, times(0)).tokenIsValid(anyString());
        verify(tokenService, times(0)).getUserId(anyString());
        verify(customerClient, times(0)).findById(anyLong());
        verify(filterChain, times(1)).doFilter(request, response);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }

    @Test
    public void mustNotAuthenticateCorrectlyAUserBecauseTokenIsNotValid() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJTaG9wIFN0eWxlIiwic3ViIjoiMjgiLCJpYXQiOjE2NTA0NTg5MTcsImV4cCI6MTY1MDQ2MjUxN30.G9fcc7pY_w9MCEKPHuLBsAc55gaQ8XI1gnUt1iowJEM");
        when(tokenService.tokenIsValid(anyString())).thenReturn(false);

        autenticatorTokenFilter.doFilterInternal(request, response, filterChain);

        verify(tokenService, times(1)).tokenIsValid(anyString());
        verify(tokenService, times(0)).getUserId(anyString());
        verify(customerClient, times(0)).findById(anyLong());
        verify(filterChain, times(1)).doFilter(request, response);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }

}
