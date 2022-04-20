package com.compass.fasttracker.history.clientFeign;

import com.compass.fasttracker.history.DTOs.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("customer")
public interface CustomerClient {

    @RequestMapping(value = "/v1/users/{id}", method = RequestMethod.GET)
    User findById(@PathVariable Long id);
}
