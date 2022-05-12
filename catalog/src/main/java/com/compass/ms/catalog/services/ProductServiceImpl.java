package com.compass.ms.catalog.services;

import com.compass.ms.catalog.DTOs.ProductDTO;
import com.compass.ms.catalog.DTOs.ProductFormDTO;
import com.compass.ms.catalog.models.Category;
import com.compass.ms.catalog.models.Product;
import com.compass.ms.catalog.repositories.CategoryRepository;
import com.compass.ms.catalog.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    ModelMapper modelMapper;

    private List<Category> checkIfVariationsIsActive(List<String> ids){
        List<Category> categories = new ArrayList<>();
        ids.forEach( category_id -> {
            Optional<Category> category = this.categoryRepository.findById(category_id);
            if(category.isPresent())
                if (category.get().isActive())
                    categories.add(category.get());
                else
                    throw new IllegalArgumentException("Categoria(s) não está(ão) ativa(s)!");  // Substituir exceção
            else
                throw new IllegalArgumentException("Categoria(s) não encontrada(s)!");          // Substituir exceção
        });
        return categories;
    }

    @Override
    public ProductDTO save(ProductFormDTO productFormDTO) {
        List<Category> categories = checkIfVariationsIsActive(productFormDTO.getCategory_ids());
        Product product = this.productRepository.save(modelMapper.map(productFormDTO,Product.class));
        categories.forEach( category -> {
            category.getProducts().add(product);
            this.categoryRepository.save(category);
        });
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public List<ProductDTO> findAll() {
        List<Product> productsList = this.productRepository.findAll();
        List<ProductDTO> productsDTOS = new ArrayList<>();
        productsList.forEach(product ->
            { productsDTOS.add(
                    modelMapper.map(product, ProductDTO.class));
            });
        return productsDTOS;
    }

    @Override
    public Optional<ProductDTO> findById(String id) {
        Optional<Product> found = this.productRepository.findById(id);
        if (found.isPresent())
            return Optional.of(modelMapper.map(found.get(),ProductDTO.class));
        return Optional.empty();
    }

    @Override
    public Optional<ProductDTO> update(ProductFormDTO form, String id) {
        Optional<Product> found = this.productRepository.findById(id);
        if (found.isPresent()){
            form.getCategory_ids().forEach( category -> {
                Optional<Category> categoryFound = this.categoryRepository.findById(category);
                if(!categoryFound.isPresent() || !categoryFound.get().isActive())
                    throw new IllegalArgumentException("Categoria(s) não está(ão) ativa(s) ou não encontrada(s)!");
            });
            found.get().setActive(form.isActive());
            found.get().setDescription(form.getDescription());
            found.get().setName(form.getName());
            Product productUpdated = this.productRepository.save(found.get());
            return Optional.of(modelMapper.map(productUpdated, ProductDTO.class));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public HttpStatus delete(String id) {
        Optional<Product> found = this.productRepository.findById(id);
        if (found.isPresent()) {
            this.productRepository.delete(found.get());
            return HttpStatus.OK;
        }
        return HttpStatus.NOT_FOUND;
    }
}
