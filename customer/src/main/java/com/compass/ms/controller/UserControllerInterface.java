package com.compass.ms.controller;

import com.compass.ms.DTO.LoginFormDTO;
import com.compass.ms.DTO.TokenDTO;
import com.compass.ms.DTO.UserDTO;
import com.compass.ms.DTO.UserFormDTO;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.transaction.Transactional;
import javax.validation.Valid;

@OpenAPIDefinition(info = @Info(title = "MS Customer",
        description = "O MS customer tem a função de armazenar e gerenciar os dados de usuário",
        version = "v1",
        license = @License(
                name = "Licença pública geral GNU v3", url = "https://www.gnu.org/licenses/gpl-3.0.pt-br.html"),
        contact = @Contact(name = "Contate o desenvolvedor", url = "https://compass.uol/"),
        termsOfService = "https://github.com/eduardo-braz/shop-Style_compass"
        ))
@Tag(name = "API MS Customer", description = "API para gerenciamento de dados dos usuários no MS")
public interface UserControllerInterface {

    @RequestBody(content = @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json",
            examples = @ExampleObject(value = exampleForm)))
    @Operation(summary = "Cadastra usuário", description = "Cadastra um novo usuário")
    @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso",
            content = @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json",
            examples = @ExampleObject(value = exampleForm)))
    @ApiResponse(responseCode = "400", description = "Parâmetro(s) inválido(s)", content =
        @Content(mediaType = "aplication/json",
                examples = @ExampleObject(value = responseBodyBadRequest)))
    @PostMapping("/users")
    @Transactional
    ResponseEntity<UserDTO> save(@RequestBody @Valid UserFormDTO body);

    @RequestBody(content = @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json",
            examples = @ExampleObject(value = exampleForm)))
    @Operation(summary = "Login de usuário", description = "Loga um usuário")
    @ApiResponse(responseCode = "200", description = "Usuário logado com sucesso",
            content = @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json",
                    examples = @ExampleObject(value = exampleForm)))
    @ApiResponse(responseCode = "400", description = "E-mail e/ou password inválido(s)", content =
    @Content(mediaType = "aplication/json",
            examples = @ExampleObject(value = responseBodyBadRequest)))
    @PostMapping("/login")
    @Transactional
    ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginFormDTO body);



    @Operation(summary = "Busca usuário", description = "Busca o usuário de {id} salvo no banco")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado",
            content = @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json",
            examples = @ExampleObject(value = exampleDto)))
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    @GetMapping("/users/{id}")
    @Transactional
    ResponseEntity<UserDTO> findId(@PathVariable Long id);



    @RequestBody(content = @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json",
            examples = @ExampleObject(value = exampleForm)))
    @Operation(summary = "Atualiza usuário", description = "Atualiza os dados do usuário com o id especificado")
    @ApiResponse(responseCode = "200", description = "Usuário alterado com sucesso",
            content = @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json",
                    examples = @ExampleObject(value = exampleForm)))
    @ApiResponse(responseCode = "400", description = "Parâmetro(s) inválido(s)", content =
        @Content(mediaType = "aplication/json",
                examples = @ExampleObject(value = responseBodyBadRequest)))
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    @PutMapping("/users/{id}")
    @Transactional
    ResponseEntity<UserDTO> update(@RequestBody @Valid UserFormDTO body, @PathVariable Long id);

    String exampleForm = "{\n" +
            "    \"firstName\": \"Maria\",\n" +
            "    \"lastName\": \"Oliveira\",\n" +
            "    \"sex\": \"Feminino\",\n" +
            "    \"cpf\": \"310.119.950-69\",\n" +
            "    \"birthdate\": \"25/12/2020\",\n" +
            "    \"email\": \"maria@email.com\",\n" +
            "    \"password\": \"12345678\",\n" +
            "    \"active\": true\n" +
            "}";

    String exampleDto = "{\n" +
            "    \"id\": 1,\n" +
            "    \"firstName\": \"Maria\",\n" +
            "    \"lastName\": \"Oliveira\",\n" +
            "    \"sex\": \"Feminino\",\n" +
            "    \"cpf\": \"310.119.950-69\",\n" +
            "    \"birthdate\": \"25/12/2020\",\n" +
            "    \"email\": \"maria@email.com\",\n" +
            "    \"password\": \"12345678\",\n" +
            "    \"active\": true\n" +
            "}";

    String responseBodyBadRequest = "{\n" +
                    "\"field\": \"password\",\n" +
                    "\"error\": \"A senha precisa de no mínimo 8 caracteres\"\n" +
            "}";
}
