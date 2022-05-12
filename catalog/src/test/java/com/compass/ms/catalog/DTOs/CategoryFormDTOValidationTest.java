package com.compass.ms.catalog.DTOs;

import com.compass.ms.catalog.controllers.CategoryController;
import com.compass.ms.catalog.exceptions.CatalogExceptionHandler;
import com.compass.ms.catalog.exceptions.InvalidOperationException;
import com.compass.ms.catalog.services.CategoryService;
import com.compass.ms.catalog.services.ProductService;
import com.compass.ms.catalog.services.VariationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static com.compass.ms.catalog.models.Instances.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@DisplayName("Testes de validação de requisição (Body)")
public class CategoryFormDTOValidationTest {

    @MockBean
    VariationService variationService;

    @MockBean
    ProductService productService;

    @MockBean
    CategoryService categoryService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CategoryController categoryController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CatalogExceptionHandler(), categoryController).build();
    }

    private void nameFieldTest(CategoryFormDTO body) throws Exception{
        when(categoryService.save(body)).thenReturn(categoryDTO());
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(
                        result -> {
                            assertTrue( result.getResolvedException() instanceof MethodArgumentNotValidException);
                            FieldError fieldError =
                                    ((MethodArgumentNotValidException) result.getResolvedException()).getFieldError();
                            assertEquals("name", fieldError.getField());
                            assertEquals("must not be blank, null or empty", fieldError.getDefaultMessage());
                        }
                );
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por Nome de categoria nulo")
    public void handleMethodArgumentNotValidExceptionBecauseCategoryNameIsNull() throws Exception {
        CategoryFormDTO body = new CategoryFormDTO(null, true);
        nameFieldTest(body);
    }

    @Test
    @DisplayName("Lança UnexpectedTypeException por Nome de categoria vazia")
    public void handleUnexpectedTypeExceptionBecauseCategoryNameIsEmpty() throws Exception {
        CategoryFormDTO body = new CategoryFormDTO("", true);
        nameFieldTest(body);
    }

    @Test
    @DisplayName("Lança UnexpectedTypeException por Nome de categoria em branco")
    public void handleUnexpectedTypeExceptionBecauseCategoryNameIsBlank() throws Exception {
        CategoryFormDTO body = new CategoryFormDTO("   ", true);
        nameFieldTest(body);
    }

    @Test
    @DisplayName("Deve retornar status 201 ao salvar categoria com atributos corretos")
    public void shouldHaveStatusCreatedWhenSaveCategoryWithCorretAttributes() throws Exception, InvalidOperationException {
        CategoryFormDTO body = categoryForm();
        Mockito.when(categoryService.save(body)).thenReturn(categoryDTO());
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(
                        result -> {
                            CategoryDTO categoryDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                                    CategoryDTO.class);
                            assertEquals(categoryDTO, categoryDTO());
                        }
                );
    }




}
