package com.compass.ms.controller;

import com.compass.ms.DTO.TokenDTO;
import com.compass.ms.DTO.UserDTO;
import com.compass.ms.DTO.UserFormDTO;
import com.compass.ms.security.SecurityConfiguration;
import com.compass.ms.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import static com.compass.ms.entity.Instances.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@DisplayName("User Controller Test")
public class UserControllerTest {

    @Autowired
    UserController userController;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SecurityConfiguration securityConfiguration;

    @MockBean
    WebSecurityConfiguration webSecurityConfiguration;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("Deve retornar status 201 ao salvar usuário")
    public void shouldHaveReturnStatusCreatedWhenSaveUser() throws Exception {
        when(userService.save(any(UserFormDTO.class))).thenReturn(userDtoInstance());
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userFormDtoInstance())))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect( result -> {
                    UserDTO userDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                            UserDTO.class);
                    assertEquals(userDtoInstance(), userDTO);
                } );
    }

    @Test
    @DisplayName("Deve retornar status 200 ao buscar usuário")
    public void shouldHaveReturnStatusOKWhenGetUserById() throws Exception {
        Long id = 1L;
        when(userService.findId(anyLong())).thenReturn(userDtoInstance());
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/v1/users/{id}",id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect( result -> {
                    UserDTO userDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                            UserDTO.class);
                    assertEquals(userDtoInstance(), userDTO);
                } );
    }

    @Test
    @DisplayName("Deve retornar status 200 ao atualizar usuário")
    public void shouldHaveReturnStatusOKWhenUpdateUser() throws Exception {
        Long id = 1L;
        when(userService.update(any(UserFormDTO.class),anyLong())).thenReturn(userDtoInstance());
        mockMvc.perform( MockMvcRequestBuilders
                        .put("/v1/users/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userFormDtoInstance())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect( result -> {
                    UserDTO userDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                            UserDTO.class);
                    assertEquals(userDtoInstance(), userDTO);
                } );
    }

    @Test
    @DisplayName("Deve retornar status 200 ao logar usuário")
    public void shouldHaveReturnStatusOKWhenLoginUser() throws Exception {
        when(userService.login(loginForm())).thenReturn(tokenDTO());
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginForm())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect( result -> {
                    TokenDTO tokenDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                            TokenDTO.class);
                    assertEquals(tokenDTO(), tokenDTO);
                } );
    }


    @Test
    @DisplayName("Deve retornar status 400 tentar salvar usuário com dados inválidos")
    public void shouldHaveReturnStatus400WhenTryingSaveUserWithInvalidParams() throws Exception {
        when(userService.save(any(UserFormDTO.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userFormDtoInstance())))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar status 404 ao buscar usuário inexistente")
    public void shouldHaveReturnStatus404WhenGetUser() throws Exception {
        when(userService.findId(-1L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/v1/users/{id}",-1L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    @DisplayName("Deve retornar status 400 tentar atualizar usuário com dados inválidos")
    public void shouldHaveReturnStatus400WhenTryingUpdateUserWithInvalidParams() throws Exception {
        when(userService.update(any(UserFormDTO.class),anyLong()))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));
        mockMvc.perform( MockMvcRequestBuilders
                        .put("/v1/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userFormDtoInstance())))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar status 404 tentar atualizar usuário de ID inválido")
    public void shouldHaveReturnStatus404WhenTryingUpdateUserWithInvalidId() throws Exception {
        when(userService.update(any(UserFormDTO.class),anyLong()))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        mockMvc.perform( MockMvcRequestBuilders
                        .put("/v1/users/{id}", -1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userFormDtoInstance())))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


}
