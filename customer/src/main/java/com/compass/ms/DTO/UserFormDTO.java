package com.compass.ms.DTO;

import com.compass.ms.entity.Sex;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class UserFormDTO {

    @NotNull(message = "Nome não pode ser nulo") @NotBlank(message = "Nome não pode estar em branco")
    @Size(min = 3, message = "Campo nome precisa de no mínimo 8 caracteres")
    private String firstName;
    @NotNull(message = "Sobrenome não pode ser nulo") @NotBlank(message = "Sobrenome não pode estar em branco")
    @Size(min = 3, message = "Campo sobrenome precisa de no mínimo 8 caracteres")
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Sex sex;
    @NotNull(message = "Campo CPF não pode ser nulo") @NotBlank(message = "Campo CPF não pode ser vazio")
    @CPF(message = "CPF Invalido")
    @Pattern(regexp = "[0-9]{3}.[0-9]{3}.[0-9]{3}-[0-9]{2}",
            message = "Formato inválido, o formato deve ser do tipo xxx.xxx.xxx-xx")
    private String cpf;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Past(message = "Data inválida")
    private LocalDate birthdate;
    @Email(message = "Email inválido")
    private String email;
    @Size(min = 8, message = "A senha precisa de no mínimo 8 caracteres")
    private String password;
    private boolean active;

    public boolean getActive(){
        return this.active;
    }
}

