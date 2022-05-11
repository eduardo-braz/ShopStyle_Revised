package com.compass.ms.entity;

import com.compass.ms.DTO.UserFormDTO;
import com.compass.ms.controller.UserController;
import com.compass.ms.exceptions.CustumerExceptionHandler;
import com.compass.ms.security.SecurityConfiguration;
import com.compass.ms.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static com.compass.ms.entity.Instances.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@DisplayName("Testes de validação de requisição (Body)")
public class UserFormDTOValidationTest {

    @MockBean
    UserService userService;

    @MockBean
    SecurityConfiguration securityConfiguration;

    @MockBean
    WebSecurityConfiguration webSecurityConfiguration;

    @Autowired
    UserController userController;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    private UserFormDTO body;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CustumerExceptionHandler(), userController).build();
        body =  userFormDtoInstance();
    }

    private void userPerformArgumentTester(String fieldErrorExpected, String messageExpected) throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(
                        result -> {
                            assertTrue( result.getResolvedException() instanceof MethodArgumentNotValidException);
                            FieldError fieldError =
                                    ((MethodArgumentNotValidException) result.getResolvedException()).getFieldError();
                            assertEquals(fieldErrorExpected, fieldError.getField());
                            assertEquals(messageExpected, fieldError.getDefaultMessage());
                        }
                );
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por FirstName nulo")
    public void handleMethodArgumentNotValidExceptionBecauseFirstNameIsNull() throws Exception {
        body.setFirstName(null);
        when(userService.save(body)).thenReturn(userDtoInstance());
        userPerformArgumentTester("firstName","Nome não pode ser nulo ou branco");

    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por FirstName em branco")
    public void handleMethodArgumentNotValidExceptionBecauseFirstNameIsEmpty() throws Exception {
        body.setFirstName("    ");
        when(userService.save(body)).thenReturn(userDtoInstance());
        userPerformArgumentTester("firstName","Nome não pode ser nulo ou branco");

    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por FirstName com menos de 3 caracteres")
    public void handleMethodArgumentNotValidExceptionBecauseFirstNameIsLessThan3Characeters() throws Exception {
        body.setFirstName("ab");
        when(userService.save(body)).thenReturn(userDtoInstance());
        userPerformArgumentTester("firstName","Campo nome precisa de no mínimo 3 caracteres");

    }


    @Test
    @DisplayName("Lança MethodArgumentNotValidException por LastName nulo")
    public void handleMethodArgumentNotValidExceptionBecauseLastNameIsNull() throws Exception {
        body.setLastName(null);
        when(userService.save(body)).thenReturn(userDtoInstance());
        userPerformArgumentTester("lastName","Sobrenome não pode ser nulo ou branco");

    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por LastName em branco")
    public void handleMethodArgumentNotValidExceptionBecauseLastNameIsEmpty() throws Exception {
        body.setLastName("   ");
        when(userService.save(body)).thenReturn(userDtoInstance());
        userPerformArgumentTester("lastName","Sobrenome não pode ser nulo ou branco");

    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por FirstName com menos de 3 caracteres")
    public void handleMethodArgumentNotValidExceptionBecauseLastNameIsLessThan3Characeters() throws Exception {
        body.setLastName("ab");
        when(userService.save(body)).thenReturn(userDtoInstance());
        userPerformArgumentTester("lastName","Campo sobrenome precisa de no mínimo 3 caracteres");

    }

    @Test
    @DisplayName("Lança IllegalArgumentException por campo Sex invalido")
    public void handleIllegalArgumentExceptionBecauseInvalidSex() throws Exception {
       String value = "fem";
        IllegalArgumentException argumentException = assertThrows(IllegalArgumentException.class, () -> {
            body.setSex(Sex.setValue(value));

        });
        assertEquals("Invalid value \'"+ value +"\'", argumentException.getMessage());
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por CPF nulo")
    public void handleMethodArgumentNotValidExceptionBecauseCpfIsNull() throws Exception {
        body.setCpf(null);
        when(userService.save(body)).thenReturn(userDtoInstance());
        userPerformArgumentTester("cpf","Campo CPF não pode ser nulo ou branco");

    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por CPF em formato inválido")
    public void handleMethodArgumentNotValidExceptionBecauseInvalidCpfFormat() throws Exception {
        body.setCpf("12345678900");
        when(userService.save(body)).thenReturn(userDtoInstance());
        userPerformArgumentTester("cpf","Formato inválido, o formato deve ser do tipo xxx.xxx.xxx-xx");

    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por CPF inválido")
    public void handleMethodArgumentNotValidExceptionBecauseInvalidCpf() throws Exception {
        body.setCpf("123.456.789-00");
        when(userService.save(body)).thenReturn(userDtoInstance());
        userPerformArgumentTester("cpf","CPF Invalido");
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por aniversário em formato inválido")
    public void handleMethodArgumentNotValidExceptionBecauseInvalidFormatBirthdate() throws Exception {
        String value = "12/25/1990";
        DateTimeParseException exception = assertThrows(DateTimeParseException.class, () -> {
            body.setBirthdate(LocalDate.parse(value));

        });
        assertEquals("Text \'"+ value +"\' could not be parsed at index 0", exception.getMessage());
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por aniversário com data futura")
    public void handleMethodArgumentNotValidExceptionBecauseFutureDateBirthdate() throws Exception {
        body.setBirthdate(LocalDate.of(2023,12,25));
        when(userService.save(body)).thenReturn(userDtoInstance());
        userPerformArgumentTester("birthdate","Data inválida");
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por email inválido")
    public void handleMethodArgumentNotValidExceptionBecauseInvalidEmail() throws Exception {
        body.setEmail("email.com");
        when(userService.save(body)).thenReturn(userDtoInstance());
        userPerformArgumentTester("email","Email inválido");
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por senha com menos de 8 caracteres")
    public void handleMethodArgumentNotValidExceptionBecausePasswordIsLessThan8Characeters() throws Exception {
        body.setPassword("abcd");
        when(userService.save(body)).thenReturn(userDtoInstance());
        userPerformArgumentTester("password","A senha precisa de no mínimo 8 caracteres");
    }


}
