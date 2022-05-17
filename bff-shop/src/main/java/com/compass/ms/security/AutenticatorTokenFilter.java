package com.compass.ms.security;

import com.compass.ms.DTOs.customer.User;
import com.compass.ms.DTOs.customer.UserDTO;
import com.compass.ms.clientFeign.CustomerClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AutenticatorTokenFilter extends OncePerRequestFilter {

    private TokenService tokenService;

    private CustomerClient customerClient;

    private ModelMapper modelMapper = new ModelMapper();

    public AutenticatorTokenFilter(TokenService tokenGenerator, CustomerClient customerClient) {
        this.tokenService = tokenGenerator;
        this.customerClient = customerClient;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader("Authorization");
        if (token!=null && !token.isEmpty() && token.startsWith("Bearer ")) {
            token = token.substring(7);
            boolean valid = tokenService.tokenIsValid(token);
            if (valid)
                userAuthenticator(token);
        }
        filterChain.doFilter(request, response);
    }

    private void userAuthenticator(String token) {
        Long userId = tokenService.getUserId(token);
        User user = modelMapper.map(customerClient.findById(userId), User.class);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }



}
