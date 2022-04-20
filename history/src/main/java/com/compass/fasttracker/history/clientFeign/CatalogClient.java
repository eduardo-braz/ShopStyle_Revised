package com.compass.fasttracker.history.clientFeign;


import com.compass.fasttracker.history.DTOs.messaging.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("catalog")
public interface CatalogClient {

    @RequestMapping(value = "/v1/variations/{id}", method = RequestMethod.GET)
    ProductDTO findProductByIdVariation(@PathVariable String id);
}
