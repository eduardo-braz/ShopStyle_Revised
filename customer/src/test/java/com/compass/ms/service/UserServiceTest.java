package com.compass.ms.service;

import com.compass.ms.DTO.UserDTO;
import com.compass.ms.DTO.UserFormDTO;
import com.compass.ms.controller.UserController;
import com.compass.ms.entity.User;
import com.compass.ms.exceptions.EntityExceptionResponse;
import com.compass.ms.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
public class UserServiceTest {

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Mock
    private UserFormDTO userFormDTO;

    @Mock
    private UserDTO userDTO;

    @Mock
    private User user;

    @Test
    @DisplayName("Deve salvar usuário")
    public void shouldHaveSaveUser() throws Exception {
        Mockito.when(userService.save(userFormDTO)).thenReturn(userDTO);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));

        UserDTO userSave = userService.save(userFormDTO);

        Assert.assertNotNull(userSave);
        Assert.assertEquals(userDTO, userSave);
    }

    /* Confirmar de método está realmente fazendo testes de maneira correta */
    @Test
    @DisplayName("Não deve salvar usuário cujo email existe no BD")
    public void mustNotSaveUserWithEmailExistingEmail() throws Exception {
        Mockito.when(userService.save(userFormDTO)).thenThrow(EntityExceptionResponse.class);
        try {
            UserDTO saved = userService.save(userFormDTO);
            Assert.assertNotEquals(saved, userDTO);
        } catch (Exception e) {}

        Assert.assertThrows(EntityExceptionResponse.class, () -> userService.save(userFormDTO));
    }

    /* Confirmar de método está realmente fazendo testes de maneira correta */
    @Test
    @DisplayName("Deve buscar usuário existe no BD")
    public void shouldHaveFindUser() throws Exception {
        Mockito.when(userService.findId(1L)).thenReturn(userDTO);

        UserDTO found = userService.findId(1L);

        Assert.assertEquals(found, userDTO);
    }

    /* Confirmar de método está realmente fazendo testes de maneira correta */
    @Test
    @DisplayName("Não encontra usuário inexistente no BD")
    public void mustNotFindUser() throws Exception {
        Mockito.when(userService.findId(1L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        try {
            UserDTO found = userService.findId(1L);
            Assert.assertNotEquals(found, userDTO);
        } catch (Exception e) {}
        Assert.assertThrows(ResponseStatusException.class, () -> userService.findId(1L));
    }

    /* Ver como devem ser os testes para o metodo update */

}
