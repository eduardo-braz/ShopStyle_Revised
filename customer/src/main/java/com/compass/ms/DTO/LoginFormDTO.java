package com.compass.ms.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class LoginFormDTO {

    @Email(message = "Email inválido")
    private String email;
    @Size(min = 8, message = "A senha precisa de no mínimo 8 caracteres")
    private String password;
}
