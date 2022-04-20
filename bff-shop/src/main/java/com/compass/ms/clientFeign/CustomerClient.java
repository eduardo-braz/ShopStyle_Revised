package com.compass.ms.clientFeign;

import com.compass.ms.DTOs.LoginFormDTO;
import com.compass.ms.DTOs.TokenDTO;
import com.compass.ms.DTOs.customer.UserDTO;
import com.compass.ms.DTOs.customer.UserFormDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("customer")
public interface CustomerClient {

    @RequestMapping(value = "/v1/users", method = RequestMethod.POST)
    UserDTO save(@RequestBody UserFormDTO body);

    @RequestMapping(value = "/v1/login", method = RequestMethod.POST)
    TokenDTO login(@RequestBody LoginFormDTO body);

    @RequestMapping(value = "/v1/users/{id}", method = RequestMethod.GET)
    UserDTO findById(@PathVariable Long id);

    @RequestMapping(value = "/v1/users/{id}", method = RequestMethod.PUT)
    UserDTO update(@RequestBody UserFormDTO body, @PathVariable Long id);

}
