package com.compass.ms.catalog.controllers;

import com.compass.ms.catalog.DTOs.*;
import com.compass.ms.catalog.exceptions.InvalidOperationException;
import com.compass.ms.catalog.exceptions.NotFoundException;
import com.compass.ms.catalog.services.CategoryService;
import com.compass.ms.catalog.services.ProductService;
import com.compass.ms.catalog.services.VariationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static com.compass.ms.catalog.models.Instances.*;
import static com.compass.ms.catalog.models.Instances.productDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@DisplayName("Category Controller Test")
public class CategoryControllerTest {

    @MockBean
    VariationService variationService;

    @MockBean
    ProductService productService;

    @MockBean
    CategoryService categoryService;

    @MockBean
    ModelMapper modelMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CategoryController categoryController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    @DisplayName("Deve retornar status 201 e category ao salvar")
    public void shouldHaveReturnStatusCreatedWhenSaveCategory() throws Exception {
        when(categoryService.save(categoryForm())).thenReturn(categoryDTO());
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryForm())))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect( result -> {
                    CategoryDTO categoryDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                            CategoryDTO.class);
                    assertEquals(objectMapper.writeValueAsString(categoryDTO()),
                            objectMapper.writeValueAsString(categoryDTO));
                } );
    }

    @Test
    @DisplayName("Deve retornar status 200 e lista de Categorias")
    public void shouldHaveReturnStatusOkWhenGetAllCategory() throws Exception {
        List<CategoryDTO> categories = new ArrayList<>();
        categories.add(categoryDTO());
        when(categoryService.findAll()).thenReturn(categories);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/v1/categories"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect( result -> {
                    List<CategoryDTO> categoryDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                            ArrayList.class);
                    assertNotNull(categoryDTO);
                    CategoryDTO dto = new ModelMapper().map(categoryDTO.get(0), CategoryDTO.class);
                    assertEquals(dto, categoryDTO());
                } );
    }

    @Test
    @DisplayName("Deve retornar status 200 ao deletar category")
    public void shouldHaveReturnStatusOkWhenDeleteCategory() throws Exception {
        when(categoryService.delete(anyString())).thenReturn(HttpStatus.OK);
        mockMvc.perform( MockMvcRequestBuilders
                        .delete("/v1/categories/{id}","abcd"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Deve retornar status 404 ao deletar category com ID inexistente")
    public void shouldHaveReturnStatusOkWhenTryDeleteCategoryWithInvalidID() throws Exception {
        when(categoryService.delete(anyString())).thenReturn(HttpStatus.NOT_FOUND);
        mockMvc.perform( MockMvcRequestBuilders
                        .delete("/v1/categories/{id}","abcd"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar status 200 e uma lista de produtos de category por id")
    public void shouldHaveReturnStatusOkWhenGetProductByCategoryId() throws Exception, NotFoundException {
        List<ProductDTO> productDTOS = new ArrayList<>();
        productDTOS.add(productDTO());
        when(categoryService.findAllProductsByCategpryId(anyString())).thenReturn(productDTOS);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/v1/categories/{id}/products","abcd"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect( result -> {
                    List<ProductDTO> productDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                            ArrayList.class);
                    assertEquals(objectMapper.writeValueAsString(productDTO()),
                            objectMapper.writeValueAsString(productDTO.get(0)));
                } );
    }

    @Test
    @DisplayName("Deve retornar status 200 ao atualizar category")
    public void shouldHaveReturnStatusOkWhenUpdateCategory() throws Exception, NotFoundException {
        when(categoryService.update(any(), anyString()))
                .thenReturn(categoryDTO());
        mockMvc.perform( MockMvcRequestBuilders
                        .put("/v1/categories/{id}","abcd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryForm())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect( result -> {
                    CategoryDTO categoryDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                            CategoryDTO.class);
                    assertEquals(objectMapper.writeValueAsString(categoryDTO()),
                            objectMapper.writeValueAsString(categoryDTO));
                } );
    }


}
