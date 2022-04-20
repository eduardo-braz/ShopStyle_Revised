package com.compass.ms.DTO;

import com.compass.ms.entity.Sex;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private Sex sex;
    private String cpf;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthdate;
    private String email;
    private boolean active;
}
