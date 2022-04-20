package com.compass.ms.catalog.services;


import com.compass.ms.catalog.DTOs.CategoryDTO;
import com.compass.ms.catalog.DTOs.CategoryFormDTO;
import com.compass.ms.catalog.DTOs.ProductDTO;
import com.compass.ms.catalog.exceptions.NotFoundException;
import com.compass.ms.catalog.models.Category;
import com.compass.ms.catalog.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CategoryDTO save(CategoryFormDTO category) {
        Category saved = categoryRepository.save(modelMapper.map(category, Category.class));
        return modelMapper.map(saved, CategoryDTO.class);
    }

    @Override
    public List<CategoryDTO> findAll() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoriesDTOS = new ArrayList<>();
        categories.forEach(category -> {
            categoriesDTOS.add(modelMapper.map(category, CategoryDTO.class));
        });
        return categoriesDTOS;
    }

    @Override
    public HttpStatus delete(String id) {
        Optional<Category> found = categoryRepository.findById(id);
        if (found.isPresent()) {
            categoryRepository.delete(found.get());
            return HttpStatus.OK;
        } else {
          return HttpStatus.NOT_FOUND;
        }
    }

    @Override
    public CategoryDTO update(CategoryFormDTO category, String id) throws NotFoundException {
        Optional<Category> categoryFound = this.categoryRepository.findById(id);
        if (categoryFound.isPresent()){
            categoryFound.get().setActive(category.isActive());
            categoryFound.get().setName(category.getName());
            Category categoryUpdated = this.categoryRepository.save(categoryFound.get());
            return modelMapper.map(categoryUpdated, CategoryDTO.class);
        }
        throw new NotFoundException();
    }

    @Override
    public List<ProductDTO> findAllProductsByCategpryId(String id) throws NotFoundException {
        Optional<Category> categoryFound = this.categoryRepository.findById(id);
        if (categoryFound.isPresent()){
            List<ProductDTO> productDTOS = new ArrayList<>();
            categoryFound.get().getProducts().forEach( product -> {
                productDTOS.add(modelMapper.map(product, ProductDTO.class));
            });
            return productDTOS;
        }
        throw new NotFoundException();
    }
}
