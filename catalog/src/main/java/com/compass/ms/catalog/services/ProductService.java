package com.compass.ms.catalog.services;

import com.compass.ms.catalog.DTOs.ProductDTO;
import com.compass.ms.catalog.DTOs.ProductFormDTO;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    public ProductDTO save(ProductFormDTO productFormDTO);

    public List<ProductDTO> findAll();

    public Optional<ProductDTO> findById(String id);

    public Optional<ProductDTO> update(ProductFormDTO form, String id);

    public HttpStatus delete(String id);
}
