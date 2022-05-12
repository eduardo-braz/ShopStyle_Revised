package com.compass.ms.catalog.controllers;

import com.compass.ms.catalog.DTOs.ProductDTO;
import com.compass.ms.catalog.DTOs.VariationDTO;
import com.compass.ms.catalog.exceptions.InvalidOperationException;
import com.compass.ms.catalog.exceptions.NotFoundException;
import com.compass.ms.catalog.services.*;
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

import static com.compass.ms.catalog.models.Instances.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@DisplayName("Variation Controller Test")
public class VariationControllerTest {

    @TestConfiguration
    public static class VariationControllerTestConfiguration{    }

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
    VariationController variationController;

    @BeforeEach
    public void setup() {
       mockMvc = MockMvcBuilders.standaloneSetup(variationController).build();
    }

    @Test
    @DisplayName("Deve retornar status 201 e variation ao salvar")
    public void shouldHaveReturnStatusCreatedWhenSaveVariation() throws Exception, InvalidOperationException {
        when(variationService.save(any())).thenReturn(variationDTO());
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/variations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(variationForm())))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect( result -> {
                    VariationDTO variationDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                            VariationDTO.class);
                    assertEquals(variationDTO, variationDTO());
                } );
    }

    @Test
    @DisplayName("Deve retornar status 200 e variation ao atualizar")
    public void shouldHaveReturnStatusOkWhenUpdateVariation() throws Exception, InvalidOperationException {
        when(variationService.update(any(),anyString())).thenReturn(variationDTO());
        mockMvc.perform( MockMvcRequestBuilders
                        .put("/v1/variations/{id}", "624f405d3b9bc442d1e0b037")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(variationForm())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect( result -> {
                    VariationDTO variationDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                            VariationDTO.class);
                    assertEquals(variationDTO, variationDTO());
                } );
    }

    @Test
    @DisplayName("Deve retornar status 200 ao deletar variation")
    public void shouldHaveReturnStatusOkWhenDeleteVariation() throws Exception, InvalidOperationException {
        when(variationService.delete(anyString())).thenReturn(HttpStatus.OK);
        mockMvc.perform( MockMvcRequestBuilders
                        .delete("/v1/variations/{id}","624f405d3b9bc442d1e0b037"))
                        .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Deve retornar status 404 ao deletar variation inválido")
    public void shouldHaveReturnStatusNotFOundWhenDeleteInvalidVariation() throws Exception, InvalidOperationException {
        when(variationService.delete(anyString())).thenReturn(HttpStatus.NOT_FOUND);
        mockMvc.perform( MockMvcRequestBuilders
                        .delete("/v1/variations/{id}","abcdf"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    @DisplayName("Deve retornar status 200 e produto ao buscar variation por ID")
    public void shouldHaveReturnStatusOkAndProductWhenFindVariationById() throws Exception, NotFoundException {
        when(variationService.findById(anyString())).thenReturn(productDTO());
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/v1/variations/{id}", "624f405d3b9bc442d1e0b037"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect( result -> {
                    ProductDTO productDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                            ProductDTO.class);
                    assertEquals(productDTO.getId(), productDTO().getId());
                } );
    }

}
