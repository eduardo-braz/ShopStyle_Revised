package com.compass.ms.controller;

import com.compass.ms.DTO.UserDTO;
import com.compass.ms.DTO.UserFormDTO;
import com.compass.ms.models.UserInstance;
import com.compass.ms.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UserFormDTO userFormDTO;

    private UserDTO userDTO;

    private URI uri;

    private UserInstance instance = new UserInstance();

    public UserControllerTest() throws URISyntaxException {
        this.uri = new URI("/v1/users");
        this.userFormDTO = instance.userFormDtoInstance();
        this.userDTO = instance.userDtoInstance();
    }

    @Test
    @DisplayName("Cria novo usuário no banco de dados")
    public void shouldHaveReturnCreatedWhenSaveInvoked() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                .post(uri)
                .content(objectMapper.writeValueAsString(userFormDTO))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers
                        .status().isCreated()
        );
    }

    @Test
    @DisplayName("Retorna BAD REQUEST quando há um parâmetro inválido")
    public void shouldHaveReturnBadRequestWhenSaveInvokedWithInvalidRequest() throws Exception {
        userFormDTO.setFirstName("Jo");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .content(objectMapper.writeValueAsString(userFormDTO))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        Assert.assertEquals(mvcResult.getResponse().getStatus(),400);
    }

    @Test
    @DisplayName("Busca usuario existente no banco (GET)")
    public void shouldHaveStatusOKWhenFindIdInvoked() throws Exception {

        Mockito.when(userService.findId(1L)).thenReturn(userDTO);

        mockMvc.perform( MockMvcRequestBuilders
                        .get("/v1/users/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    @DisplayName("Retorna NOT FOUND ao buscar usuario inexistente")
    public void shouldHaveStatusNotFoundWhenDontFindId() throws Exception {


        Mockito.when(userService.findId(1L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform( MockMvcRequestBuilders
                        .get("/v1/users/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

    }

    @Test
    @DisplayName("Altera usuário no banco de dados")
    public void shouldHaveReturnOkWhenUpdateInvoked() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                .put("/v1/users/{id}", 1)
                .content(objectMapper.writeValueAsString(userFormDTO))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(
                MockMvcResultMatchers
                        .status().isOk())
        ;
    }

    @Test
    @DisplayName("Retorna BAD REQUEST ao tentar cadastrar usuário com dado inválido")
    public void shouldHaveBadRequestWhenUpdateInvoked() throws Exception {
        userFormDTO.setFirstName("ab");

        mockMvc.perform( MockMvcRequestBuilders
                        .put("/v1/users/{id}", 1)
                        .content(objectMapper.writeValueAsString(userFormDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        MockMvcResultMatchers
                                .status().isBadRequest());
    }

    /* o método abaixo está sempre retornando status 200, mas deveria ser 404 */
    @Test
    @DisplayName("Retorna NOT FOUND ao tentar cadastrar usuário com ID inválido")
    public void shouldHaveNotFoundWhenUpdateInvoked() throws Exception {
        Long id = -1L;
        Mockito.when(userService.findId(id)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        userDTO.setId(-1L);
        mockMvc.perform( MockMvcRequestBuilders
                        .put("/v1/users/{id}", id)
                        .content(objectMapper.writeValueAsString(userFormDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }
}
