package com.compass.ms.catalog.controllers;

import com.compass.ms.catalog.DTOs.ProductDTO;
import com.compass.ms.catalog.services.CategoryService;
import com.compass.ms.catalog.services.ProductService;
import com.compass.ms.catalog.services.VariationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static com.compass.ms.catalog.models.Instances.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@DisplayName("Product Controller Test")
public class ProductControllerTest {

    @TestConfiguration
    public static class ProductControllerTestConfiguration{    }

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
    ProductController productController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    @DisplayName("Deve retornar status 201 e product ao salvar")
    public void shouldHaveReturnStatusCreatedWhenSaveProduct() throws Exception {
        when(productService.save(productForm())).thenReturn(productDTO());
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productForm())))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect( result -> {
                    ProductDTO productDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                            ProductDTO.class);
                    assertEquals(objectMapper.writeValueAsString(productDTO()),
                            objectMapper.writeValueAsString(productDTO));
                } );
    }

    @Test
    @DisplayName("Deve retornar status 200 e lista de produtos")
    public void shouldHaveReturnStatusOkWhenGetAllProduct() throws Exception {
        when(productService.findAll()).thenReturn(productDTOList());
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/v1/products"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect( result -> {
                    List<ProductDTO> productDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                            ArrayList.class);
                    assertNotNull(productDTO);
                    ProductDTO dto = new ModelMapper().map(productDTO.get(0), ProductDTO.class);
                    assertEquals(objectMapper.writeValueAsString(productDTO()),
                            objectMapper.writeValueAsString(dto));
                } );
    }

    @Test
    @DisplayName("Deve retornar status 200 e um produto por ID")
    public void shouldHaveReturnStatusOkWhenGetProductById() throws Exception{
        when(productService.findById(anyString())).thenReturn(Optional.of(productDTO()));
        String id = "624f40363b9bc442d1e0b036";
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/v1/products/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect( result -> {
                    ProductDTO productDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                            ProductDTO.class);
                    assertEquals(objectMapper.writeValueAsString(productDTO()),
                            objectMapper.writeValueAsString(productDTO));
                } );
    }

    @Test
    @DisplayName("Deve retornar status 404 ao buscar produto inexistente")
    public void shouldHaveReturnStatusNotFoundWhenGetInvalidProductById() throws Exception{
        when(productService.findById(anyString())).thenReturn(Optional.empty());
        String id = "624f40363b9bc442d1e0b036";
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/v1/products/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar status 200 ao deletar produto")
    public void shouldHaveReturnStatusOkWhenDeleteProduct() throws Exception{
        String id = "624f40363b9bc442d1e0b036";
        when(productService.delete(id)).thenReturn(HttpStatus.OK);
        mockMvc.perform( MockMvcRequestBuilders
                        .delete("/v1/products/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Deve retornar status 404 ao tentar deletar produto")
    public void shouldHaveReturnStatusNotFoundWhenTryDeleteProduct() throws Exception{
        String id = "624f40363b9bc442d1e0b036";
        when(productService.delete(id)).thenReturn(HttpStatus.OK);
        mockMvc.perform( MockMvcRequestBuilders
                        .delete("/v1/products/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Deve retornar status 200 ao deletar produto")
    public void shouldHaveReturnStatusOkWhenUpdateProduct() throws Exception{
        String id = "624f40363b9bc442d1e0b036";
        when(productService.update(eq(productForm()), eq(id))).thenReturn(Optional.of(productDTO()));
        mockMvc.perform( MockMvcRequestBuilders
                        .put("/v1/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productForm())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect( result -> {
                    ProductDTO productDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                            ProductDTO.class);
                    assertEquals(objectMapper.writeValueAsString(productDTO()),
                            objectMapper.writeValueAsString(productDTO));
                } );
    }

    @Test
    @DisplayName("Deve retornar status 404 ao tentar deletar produto inexistente")
    public void shouldHaveReturnStatusNotFoundWhenTryUpdateProduct() throws Exception{
        String id = "624f40363b9bc442d1e0b036";
        when(productService.update(eq(productForm()), eq(id))).thenReturn(Optional.empty());
        mockMvc.perform( MockMvcRequestBuilders
                        .put("/v1/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productForm())))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
