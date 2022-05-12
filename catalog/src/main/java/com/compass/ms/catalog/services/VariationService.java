package com.compass.ms.catalog.services;


import com.compass.ms.catalog.DTOs.ProductDTO;
import com.compass.ms.catalog.DTOs.VariationDTO;
import com.compass.ms.catalog.DTOs.VariationFormDTO;
import com.compass.ms.catalog.exceptions.InvalidOperationException;
import com.compass.ms.catalog.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;



public interface VariationService {

    public VariationDTO save(VariationFormDTO form) throws InvalidOperationException;

    public VariationDTO update(VariationFormDTO form, String id) throws InvalidOperationException;

    public HttpStatus delete(String id);

    public ProductDTO findById(String id) throws NotFoundException;


}
