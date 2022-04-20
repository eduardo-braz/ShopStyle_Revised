package com.compass.ms.clientFeign;

import com.compass.ms.DTOs.catalog.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("catalog")
public interface CatalogClient {

    @RequestMapping(value = "/v1/products/{id}", method = RequestMethod.GET)
    ProductDTO getProduct(@PathVariable String id);

    @RequestMapping(value = "/v1/categories/{id}/products", method = RequestMethod.GET)
    List<ProductDTO> getProductsByCategories(@PathVariable String id);


}
