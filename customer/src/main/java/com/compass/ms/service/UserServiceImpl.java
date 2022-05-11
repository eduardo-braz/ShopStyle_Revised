package com.compass.ms.service;

import com.compass.ms.DTO.LoginFormDTO;
import com.compass.ms.DTO.TokenDTO;
import com.compass.ms.DTO.UserDTO;
import com.compass.ms.DTO.UserFormDTO;
import com.compass.ms.entity.User;
import com.compass.ms.exceptions.EntityExceptionResponse;
import com.compass.ms.repository.UserRepository;
import com.compass.ms.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public TokenDTO login(LoginFormDTO body) {
        Optional<User> findUser = this.userRepository.findByEmail(body.getEmail());
        if (findUser.isPresent()){
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken( body.getEmail(), body.getPassword() ));
                String token = tokenService.generate(authentication);
                return new TokenDTO(token, "Bearer");
            } catch (AuthenticationException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        }
        throw new EntityExceptionResponse(HttpStatus.NOT_FOUND,"Email não encontrado.");
    }

    @Override
    public UserDTO save(UserFormDTO formDTO) {
        Optional<User> findUser = this.userRepository.findByEmail(formDTO.getEmail());
        if (findUser.isPresent()) {
            throw new EntityExceptionResponse(HttpStatus.BAD_REQUEST,
                    "Email " + formDTO.getEmail() + " existente no banco");
        } else {
            formDTO.setPassword(new BCryptPasswordEncoder().encode(formDTO.getPassword()));
            User user = this.userRepository.save(modelMapper.map(formDTO, User.class));
            return modelMapper.map(user, UserDTO.class);
        }
    }

    @Override
    public UserDTO findId(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isPresent())
            return modelMapper.map(user.get(), UserDTO.class);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public UserDTO update(UserFormDTO formDTO, Long id) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isPresent()) {
            Optional<User> validUser = this.userRepository.findByEmailAndId(formDTO.getEmail(), id);
            if (validUser.isPresent()) {
                // Já existe um email igual cadastrado no banco
                throw new EntityExceptionResponse(HttpStatus.BAD_REQUEST,
                        "Email " + formDTO.getEmail() + " existente no banco");
            }
            // Atualizando dados
            user.get().setActive(formDTO.getActive());
            user.get().setCpf(formDTO.getCpf());
            user.get().setBirthdate(formDTO.getBirthdate());
            user.get().setEmail(formDTO.getEmail());
            user.get().setFirstName(formDTO.getFirstName());
            user.get().setLastName(formDTO.getLastName());
            user.get().setSex(formDTO.getSex());
            // Atualizando senha
            user.get().setPassword(new BCryptPasswordEncoder().encode(formDTO.getPassword()));
            return modelMapper.map(user.get(), UserDTO.class);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}