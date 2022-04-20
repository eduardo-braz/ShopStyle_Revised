package com.compass.fasttracker.history.clientFeign;

import com.compass.fasttracker.history.DTOs.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("checkout")
public interface CheckoutClient {

    @RequestMapping(value = "/v1/payments/{id}", method = RequestMethod.GET)
    Payment findById(@PathVariable Long id);

}
