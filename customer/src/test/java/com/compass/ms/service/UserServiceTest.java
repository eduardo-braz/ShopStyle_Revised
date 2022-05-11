package com.compass.ms.service;

import com.compass.ms.DTO.UserDTO;
import com.compass.ms.entity.User;
import com.compass.ms.exceptions.EntityExceptionResponse;
import com.compass.ms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.compass.ms.entity.Instances.*;
import static com.compass.ms.entity.Instances.userInstance;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DisplayName("User Service Test")
public class UserServiceTest {

    @MockBean
    UserRepository userRepository;

    UserService userService;

    @BeforeEach
    public void init(){
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    @DisplayName("Deve salvar um usuário")
    public void shouldHaveSaveUser(){
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(userInstance());

        UserDTO saved = userService.save(userFormDtoInstance());
        succesfullCaseAssertions(userDtoInstance(), saved);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar salva usuário cujo email exista no banco")
    public void shouldHaveThrowExceptionWhenSaveUserIfUserEmailExists(){
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userInstance()));
        EntityExceptionResponse exception = assertThrows(EntityExceptionResponse.class, () -> {
            userService.save(userFormDtoInstance());
        });
        emailAssertions(exception);
    }

    @Test
    @DisplayName("Deve buscar usuário por ID")
    public void shouldHaveFindUserById(){
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userInstance()));
        UserDTO found = userService.findId(1L);
        succesfullCaseAssertions(userDtoInstance(), found);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar usuário inexistente")
    public void shouldHaveThrowExceptionWhenFindUserByInvalidId(){
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.findId(1L);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

    }

    @Test
    @DisplayName("Deve atualizar dados de um usuário")
    public void shouldHaveUpdateUser(){
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userInstance()));
        when(userRepository.findByEmailAndId(anyString(), anyLong())).thenReturn(Optional.empty());
        UserDTO updated = userService.update(userFormDtoInstance(), 1L);
        succesfullCaseAssertions(userDtoInstance(), updated);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar usuário inexistente")
    public void shouldHaveThrowExceptionWhenUpdateUserByInvalidId() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.update(userFormDtoInstance(),1L);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar usuário com email existente")
    public void shouldHaveThrowExceptionWhenUpdateUserWithEmailExists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userInstance()));
        when(userRepository.findByEmailAndId(anyString(), anyLong())).thenReturn(Optional.of(userInstance()));

        EntityExceptionResponse exception = assertThrows(EntityExceptionResponse.class, () -> {
            userService.update(userFormDtoInstance(),1L);
        });

        emailAssertions(exception);
    }

    private void emailAssertions(EntityExceptionResponse exception){
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        String expected = "Email " + userInstance().getEmail() + " existente no banco";
        assertEquals(expected, exception.getMessage());
    }

    private void succesfullCaseAssertions(Object expected, Object actual){
        assertNotNull(actual);
        assertEquals(expected, actual);
    }


}
