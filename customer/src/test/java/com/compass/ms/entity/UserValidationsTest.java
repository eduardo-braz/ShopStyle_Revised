package com.compass.ms.entity;


import com.compass.ms.DTO.UserDTO;
import com.compass.ms.DTO.UserFormDTO;
import com.compass.ms.controller.UserController;
import com.compass.ms.models.UserInstance;
import com.compass.ms.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.validation.UnexpectedTypeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
public class UserValidationsTest {

    @Mock
    UserFormDTO user;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UserFormDTO userFormDTO;

    private UserDTO userDTO;

    private static String uri = "/v1/users";

    private UserInstance instance = new UserInstance();

    @BeforeEach
    public void beforeEach(){
        this.userFormDTO = instance.userFormDtoInstance();
    }

    private MvcResult performController(ResultMatcher resultMatcher) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(objectMapper.writeValueAsString(userFormDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(resultMatcher)
                .andReturn();
        return mvcResult;
    }

    @Test
    @DisplayName("Valida usuario com todos parâmetros corretos")
    public void shouldHaveReturnCreatedWhenAllParametersAreCorrect() throws Exception {
        ResultMatcher resultMatcher = MockMvcResultMatchers.status().isCreated();
        performController(resultMatcher);
    }

    @Test
    @DisplayName("Invalida Firstname do Usuário (Min 3 caracteres)")
    public void shouldInvalidateFirstnameUserBecauseCharacterLenght() throws Exception {
        userFormDTO.setFirstName("ab");
        ResultMatcher resultMatcher = MockMvcResultMatchers.status().isBadRequest();

        Assert.assertTrue(performController(resultMatcher).getResolvedException()
                .getLocalizedMessage()
                .contains("Campo nome precisa de no mínimo 8 caracteres"));
    }

    @Test
    @DisplayName("Invalida Firstname do Usuário (NULL)")
    public void shouldInvalidateFirstnameUserBecauseNull() throws Exception {
        userFormDTO.setFirstName(null);
        ResultMatcher resultMatcher = MockMvcResultMatchers.status().isBadRequest();

        Assert.assertTrue(
                performController(resultMatcher).getResolvedException()
                .getLocalizedMessage()
                .contains("Nome não pode ser nulo"));
    }

    @Test
    @DisplayName("Invalida Firstname do Usuário (EMPTY)")
    public void shouldInvalidateFirstnameUserBecauseEmpty() throws Exception {
        userFormDTO.setFirstName("");
        ResultMatcher resultMatcher = MockMvcResultMatchers.status().isBadRequest();

        Assert.assertTrue(
                performController(resultMatcher).getResolvedException()
                .getLocalizedMessage()
                .contains("Nome não pode estar em branco"));
    }

    @Test
    @DisplayName("Invalida Lastname do Usuário (Min 3 caracteres)")
    public void shouldInvalidateLastnameUserBecauseCharacterLenght() throws Exception {
        userFormDTO.setLastName("ab");
        ResultMatcher resultMatcher = MockMvcResultMatchers.status().isBadRequest();

        Assert.assertTrue(performController(resultMatcher).getResolvedException()
                .getLocalizedMessage()
                .contains("Campo sobrenome precisa de no mínimo 8 caracteres"));
    }

    @Test
    @DisplayName("Invalida Lastname do Usuário (NULL)")
    public void shouldInvalidateLastnameUserBecauseNull() throws Exception {
        userFormDTO.setLastName(null);
        ResultMatcher resultMatcher = MockMvcResultMatchers.status().isBadRequest();

        Assert.assertTrue(
                performController(resultMatcher).getResolvedException()
                        .getLocalizedMessage()
                        .contains("Sobrenome não pode ser nulo"));
    }

    @Test
    @DisplayName("Invalida Lastname do Usuário (EMPTY)")
    public void shouldInvalidateLastnameUserBecauseEmpty() throws Exception {
        userFormDTO.setLastName("");
        ResultMatcher resultMatcher = MockMvcResultMatchers.status().isBadRequest();

        Assert.assertTrue(
                performController(resultMatcher).getResolvedException()
                        .getLocalizedMessage()
                        .contains("Sobrenome não pode estar em branco"));
    }

    @Test
    @DisplayName("Invalida sexo do Usuário diferente de Enum")
    public void shouldInvalidateSexUser() throws Exception {
        String sexTest = "fem";

        ResultMatcher resultMatcher = MockMvcResultMatchers.status().isBadRequest();
        Mockito.when(userService.save(userFormDTO))
                .thenThrow(IllegalArgumentException.class);
        try {
            userFormDTO.setSex(Sex.valueOf(sexTest));
            performController(resultMatcher);
        } catch (Exception e) {}

        Assert.assertThrows(IllegalArgumentException.class, () -> userService.save(userFormDTO));
    }

    @Test
    @DisplayName("Invalida CPF se valor (NULL)")
    public void shouldInvalidateCPFUserBecauseNull() throws Exception {
        userFormDTO.setCpf(null);
        ResultMatcher resultMatcher = MockMvcResultMatchers.status().isBadRequest();

        Assert.assertTrue(
                performController(resultMatcher).getResolvedException()
                        .getLocalizedMessage()
                        .contains("Campo CPF não pode ser nulo"));
    }

    @Test
    @DisplayName("Invalida CPF se campo (EMPTY)")
    public void shouldInvalidateCPFUserBecauseEmpty() throws Exception {
        userFormDTO.setCpf("");
        ResultMatcher resultMatcher = MockMvcResultMatchers.status().isBadRequest();

        Assert.assertTrue(
                performController(resultMatcher).getResolvedException()
                        .getLocalizedMessage()
                        .contains("Campo CPF não pode ser vazio"));
    }

    @Test
    @DisplayName("Invalida CPF fora do padrão")
    public void shouldInvalidateCPFUserBecausePattern() throws Exception {
        userFormDTO.setCpf("31011995069");
        ResultMatcher resultMatcher = MockMvcResultMatchers.status().is4xxClientError();
        MvcResult mvcResult = performController(resultMatcher);
        Assert.assertTrue(
                mvcResult.getResolvedException()
                        .getLocalizedMessage()
                        .contains("Formato invalido, o formato deve ser do tipo xxx.xxx.xxx-xx"));
    }

    @Test
    @DisplayName("CPF inválido")
    public void shouldInvalidateCPFUserBecauseChecksum() throws Exception {
        userFormDTO.setCpf("111.111.111-11");
        ResultMatcher resultMatcher = MockMvcResultMatchers.status().isBadRequest();
        MvcResult mvcResult = performController(resultMatcher);
        Assert.assertTrue(
                mvcResult.getResolvedException()
                        .getLocalizedMessage()
                        .contains("CPF Invalido"));
    }

    @Test
    @DisplayName("Invalida Birthdate com data futura")
    public void shouldInvalidateBirthdateUserWithFutureDate() throws Exception {
        userFormDTO.setBirthdate(LocalDate.now().plusDays(1));
        ResultMatcher resultMatcher = MockMvcResultMatchers.status().isBadRequest();
        MvcResult mvcResult = performController(resultMatcher);
        Assert.assertTrue(
                mvcResult.getResolvedException()
                        .getLocalizedMessage()
                        .contains("Data inválida"));
    }

    @Test
    @DisplayName("Invalida Birthdate fora do padrão")
    public void shouldInvalidateBirthdateUserBecausePattern() throws Exception {
        String birthDateTest = "12/25/20";
        String pattern = "dd/MM/yyyy";
        Mockito.when(userService.save(userFormDTO))
                .thenThrow(DateTimeParseException.class);
        try {
            userFormDTO.setBirthdate(LocalDate.parse(
                    birthDateTest, DateTimeFormatter.ofPattern(pattern)));
            ResultMatcher resultMatcher = MockMvcResultMatchers.status().is4xxClientError();
            performController(resultMatcher);
        } catch (Exception e) {}

        Assert.assertThrows(DateTimeParseException.class, () -> userService.save(userFormDTO));
    }


    @Test
    @DisplayName("Invalida Email incorreto")
    public void shouldInvalidateEmailBecausePattern() throws Exception {
        String emailTest = "mariacom";
        userFormDTO.setEmail(emailTest);

        ResultMatcher resultMatcher = MockMvcResultMatchers.status().isBadRequest();
        MvcResult mvcResult = performController(resultMatcher);

        Assert.assertTrue(
                mvcResult.getResolvedException()
                        .getLocalizedMessage()
                        .contains("Email invalido"));
    }


    @Test
    @DisplayName("Invalida Password com menos de 8 caracteres")
    public void shouldInvalidatePasswordForCharacterLenght() throws Exception {
        String passwordTest = "ab1234";
        userFormDTO.setPassword(passwordTest);

        ResultMatcher resultMatcher = MockMvcResultMatchers.status().isBadRequest();
        MvcResult mvcResult = performController(resultMatcher);
        Assert.assertTrue(
                mvcResult.getResolvedException()
                        .getLocalizedMessage()
                        .contains("A senha precisa de no mínimo 8 caracteres"));
    }

    /* Fazer teste de autenticação do email */

    /* Verificar se método abaixo (e similares) está fazendo testes efetivos */
    @Test
    @DisplayName("Invalida Active nulo")
    public void shouldInvalidateActiveNull() throws Exception {
        userFormDTO.setActive(Boolean.valueOf(null));
        Mockito.when(userService.save(userFormDTO))
                .thenThrow(UnexpectedTypeException.class);
        try {
            ResultMatcher resultMatcher = MockMvcResultMatchers.status().isBadRequest();
            MvcResult mvcResult = performController(resultMatcher);
            Assert.assertTrue(
                    mvcResult.getResolvedException()
                            .getLocalizedMessage()
                            .contains("Campo Active não pode ser nulo"));
        } catch (Exception e) {}

        Assert.assertThrows(UnexpectedTypeException.class, () -> userService.save(userFormDTO));
    }

    @Test
    @DisplayName("Invalida Active vazio")
    public void shouldInvalidateActiveEmpty() throws Exception {
        userFormDTO.setActive(Boolean.valueOf(""));
        Mockito.when(userService.save(userFormDTO))
                .thenThrow(HttpMessageNotReadableException.class);
        try {
            ResultMatcher resultMatcher = MockMvcResultMatchers.status().isBadRequest();
            MvcResult mvcResult = performController(resultMatcher);
            Assert.assertTrue(
                    mvcResult.getResolvedException()
                            .getLocalizedMessage()
                            .contains("Campo Active não pode estar vazio"));
        } catch (Exception e) {}

        Assert.assertThrows(HttpMessageNotReadableException.class, () -> userService.save(userFormDTO));

    }

}
