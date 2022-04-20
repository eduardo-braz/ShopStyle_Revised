package com.compass.ms.models;

import com.compass.ms.DTO.UserDTO;
import com.compass.ms.DTO.UserFormDTO;
import com.compass.ms.entity.Sex;
import com.compass.ms.entity.User;
import javafx.util.converter.LocalDateStringConverter;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

@NoArgsConstructor
public class UserInstance {

    ModelMapper modelMapper = new ModelMapper();

    public User userInstance(){
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

    public UserDTO userDtoInstance() {
        UserDTO dto = new UserDTO();
        dto = this.modelMapper.map(userInstance(), UserDTO.class);
        dto.setId(1L);
        return dto;
    }

    public UserFormDTO userFormDtoInstance() {
        UserFormDTO form = new UserFormDTO();
        form = this.modelMapper.map(userDtoInstance(), UserFormDTO.class);
        return form;
    }
}
