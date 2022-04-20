package com.compass.ms.catalog.services;


import com.compass.ms.catalog.DTOs.CategoryDTO;
import com.compass.ms.catalog.DTOs.CategoryFormDTO;
import com.compass.ms.catalog.DTOs.ProductDTO;
import com.compass.ms.catalog.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface CategoryService {

        public CategoryDTO save(CategoryFormDTO category);

        public List<CategoryDTO> findAll();

        public HttpStatus delete(String id);

        public CategoryDTO update (CategoryFormDTO category, String id) throws NotFoundException;

        public List<ProductDTO> findAllProductsByCategpryId(String id) throws NotFoundException;


}
