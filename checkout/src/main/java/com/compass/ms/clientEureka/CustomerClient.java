package com.compass.ms.clientEureka;

import com.compass.ms.DTO.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("customer")
public interface CustomerClient {

    @RequestMapping(value = "/v1/users/{id}", method = RequestMethod.GET)
    UserDTO findId(@PathVariable Long id);
}
