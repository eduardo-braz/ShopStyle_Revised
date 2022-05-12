package com.compass.ms.catalog.services;

import com.compass.ms.catalog.DTOs.CategoryDTO;
import com.compass.ms.catalog.DTOs.CategoryFormDTO;
import com.compass.ms.catalog.DTOs.ProductDTO;
import com.compass.ms.catalog.exceptions.NotFoundException;
import com.compass.ms.catalog.models.Category;
import com.compass.ms.catalog.models.Instances;
import com.compass.ms.catalog.repositories.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.compass.ms.catalog.models.Instances.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("Category Service Test")
public class CategoryServiceTest {

    @TestConfiguration
    static class CategoryServiceTestConfiguration{
        @Bean
        public CategoryService categoryService(){
            return new CategoryServiceImpl();
        }

        @Bean
        public ModelMapper modelMapper(){
            return new ModelMapper();
        }
    }

    @MockBean
    CategoryRepository categoryRepository;

    @Autowired
    CategoryService categoryService;

    @Test
    @DisplayName("Deve retornar objeto CategoryDTO ao salvar uma categoria")
    public void shouldHaveReturnCategoryDtoWhenSaveCategory(){
        when(categoryRepository.save(any(Category.class))).thenReturn(category());
        CategoryDTO saved = categoryService.save(categoryForm());
        assertNotNull(saved);
        assertEquals(categoryDTO(), saved);
    }

    @Test
    @DisplayName("Deve retornar lista de CategoryDTO ao buscar todas categorias")
    public void shouldHaveReturnListOfCategoryDtoWhenGetAllCategories(){
        List<Category> categories = new ArrayList<>();
        categories.add(category());
        categories.add(category());
        when(categoryRepository.findAll()).thenReturn(categories);

        List<CategoryDTO> getAll = categoryService.findAll();
        assertNotNull(getAll);
        assertEquals(categoryDTO(), getAll.get(0));
        assertEquals(categoryDTO(), getAll.get(1));
    }

    @Test
    @DisplayName("Deve retornar satus 200 ao deletar uma categoria")
    public void shouldHaveReturnStatusOkWhenDeleteCategory(){
        when(categoryRepository.findById(anyString())).thenReturn(Optional.of(category()));
        HttpStatus status = categoryService.delete("abcd");
        assertNotNull(status);
        assertEquals(HttpStatus.OK, status);
    }

    @Test
    @DisplayName("Deve retornar satus 404 ao tentar deletar uma categoria inexistente")
    public void shouldHaveReturnStatusNotFoundWhenTryDeleteInvalidCategory(){
        when(categoryRepository.findById(anyString())).thenReturn(Optional.empty());
        HttpStatus status = categoryService.delete("abcd");
        assertNotNull(status);
        assertEquals(HttpStatus.NOT_FOUND, status);
    }

    @Test
    @DisplayName("Deve retornar satus 200 e CategoryDTO ao atualizar uma categoria")
    public void shouldHaveReturnOkFoundWhenUpdateCategory() throws NotFoundException {
        Category update = category();
        update.setName("New name");
        update.setActive(false);
        when(categoryRepository.findById(anyString())).thenReturn(Optional.of(category()));
        when(categoryRepository.save(any(Category.class))).thenReturn(update);
        CategoryDTO updated = categoryService.update(categoryForm(), "abcd");
        assertNotNull(updated);
        assertNotEquals(categoryDTO(), updated);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao tentar atualizar uma categoria inexistente")
    public void shouldHaveThrowNotFoundExceptionWhenTryUpdateInvalidCategory(){
        when(categoryRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {
            CategoryDTO updated = categoryService.update(categoryForm(), "abcd");
            assertNull(updated);
        });
    }

    @Test
    @DisplayName("Deve retornar lista com todos os produtos de uma determinada categoria")
    public void shouldHaveReturnListOfPRoductsWhenGetProductsByCategoryId() throws NotFoundException {
        when(categoryRepository.findById(anyString())).thenReturn(Optional.of(category()));
        ProductDTO productOne = new ModelMapper().map(productOne(), ProductDTO.class);
        ProductDTO productTwo = new ModelMapper().map(productTwo(), ProductDTO.class);
        List<ProductDTO> products = categoryService.findAllProductsByCategpryId("abcd");
        assertNotNull(products);
        assertEquals(productOne, products.get(0));
        assertEquals(productTwo, products.get(1));
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao buscar produtos de categoria inexistente")
    public void shouldHaveThrowNotFoundExceptionWhenTryGetProductsOfInvalidCategory(){
        when(categoryRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {
            List<ProductDTO> products = categoryService.findAllProductsByCategpryId("abcd");
            assertNull(products);
        });
    }

}
