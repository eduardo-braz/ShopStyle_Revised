package com.compass.ms.controller;

import com.compass.ms.DTO.LoginFormDTO;
import com.compass.ms.DTO.TokenDTO;
import com.compass.ms.DTO.UserDTO;
import com.compass.ms.DTO.UserFormDTO;
import com.compass.ms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RequestMapping("/v1")
@RestController
public class UserController implements UserControllerInterface{

    @Autowired
    private UserService userService;

    @Override
    @PostMapping("/users")
    @Transactional
    public ResponseEntity<UserDTO> save(@RequestBody @Valid UserFormDTO body){
        return new ResponseEntity<>(this.userService.save(body),
                HttpStatus.CREATED);
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginFormDTO body) {
        return new ResponseEntity<>(this.userService.login(body),
                HttpStatus.OK);
    }

    @Override
    @GetMapping("/users/{id}")
    @Transactional
    public ResponseEntity<UserDTO> findId(@PathVariable Long id){
        return new ResponseEntity<>(this.userService.findId(id),
                HttpStatus.OK);
    }


    @Override
    @PutMapping("/users/{id}")
    @Transactional
    public ResponseEntity<UserDTO> update(@RequestBody @Valid UserFormDTO body, @PathVariable Long id){
        return new ResponseEntity<>(this.userService.update(body, id),
                HttpStatus.OK);
    }

}
