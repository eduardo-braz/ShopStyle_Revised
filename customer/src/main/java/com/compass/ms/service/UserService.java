package com.compass.ms.service;

import com.compass.ms.DTO.LoginFormDTO;
import com.compass.ms.DTO.TokenDTO;
import com.compass.ms.DTO.UserDTO;
import com.compass.ms.DTO.UserFormDTO;

public interface UserService {

    TokenDTO login(LoginFormDTO body);

    UserDTO save(UserFormDTO formDTO);

    UserDTO findId(Long id);

    UserDTO update(UserFormDTO formDTO, Long id);

}
