package com.compass.ms.entity;

import com.compass.ms.DTO.LoginFormDTO;
import com.compass.ms.DTO.TokenDTO;
import com.compass.ms.DTO.UserDTO;
import com.compass.ms.DTO.UserFormDTO;
import javafx.util.converter.LocalDateStringConverter;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

public class Instances {

    public static User userInstance(){
        User user = new User();
        user.setFirstName("Maria");
        user.setLastName("Oliveira");
        user.setSex(Sex.Feminino);
        user.setCpf("310.119.950-69");
        LocalDate birthDate = new LocalDateStringConverter().fromString("25/12/2020");
        user.setBirthdate(birthDate);
        user.setEmail("mariater@email.com");
        user.setPassword("12345678");
        user.setActive(true);
        return user;
    }

    public static UserDTO userDtoInstance() {
        ModelMapper mapper = new ModelMapper();
        UserDTO dto = mapper.map(userInstance(), UserDTO.class);
        return dto;
    }

    public static UserFormDTO userFormDtoInstance() {
        return new ModelMapper().map(userInstance(), UserFormDTO.class);
    }

    public static LoginFormDTO loginForm(){
        return new LoginFormDTO("mariater@email.com", "12345678");
    }

    public static TokenDTO tokenDTO(){
        return new TokenDTO(
    "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJTaG9wIFN0eWxlIiwic3ViIjoiMjgiLCJpYXQiOjE2NTE2MDg2MTMsImV4cCI6MTY1MTYxMjIxM30.a-g7zHJD3V5beaE99UNQf4eHpRGlLtu6eNskdSHm8NE",
     "Bearer"
        );
    }


}
