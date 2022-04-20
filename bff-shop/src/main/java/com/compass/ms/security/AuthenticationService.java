package com.compass.ms.security;

import com.compass.ms.DTOs.customer.User;
import com.compass.ms.DTOs.customer.UserDTO;
import com.compass.ms.clientFeign.CustomerClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService implements UserDetailsService{

    @Autowired
    CustomerClient customerClient;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        try {
            UserDTO usuario = customerClient.findById(Long.parseLong(id));
            return modelMapper.map(usuario, User.class);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Email e/ou password inválido(s).");
        }
    }
}
