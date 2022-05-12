package com.compass.ms.catalog.DTOs;

import com.compass.ms.catalog.controllers.ProductController;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.compass.ms.catalog.models.Instances.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@DisplayName("Testes de validação de requisição (Body)")
public class ProductFormDTOValidationTest {

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
    ProductController productController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CatalogExceptionHandler(), productController).build();
    }

    private void nameAndDescriptionFieldTest(ProductFormDTO body, String field, String message) throws Exception{
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(
                        result -> {
                            assertTrue( result.getResolvedException() instanceof MethodArgumentNotValidException);
                            FieldError fieldError =
                                    ((MethodArgumentNotValidException) result.getResolvedException()).getFieldError();
                            assertEquals(field, fieldError.getField());
                            assertEquals(message, fieldError.getDefaultMessage());
                        }
                );
    }

    private void categoryIdsTest(ProductFormDTO body, String message) throws Exception{
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(
                        result -> {
                            assertTrue( result.getResolvedException() instanceof MethodArgumentNotValidException);
                            FieldError fieldError =
                                    ((MethodArgumentNotValidException) result.getResolvedException()).getFieldError();
                            assertTrue(fieldError.getField().startsWith("category_ids"));
                            assertEquals(message, fieldError.getDefaultMessage());
                        }
                );
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por nome de produto nulo")
    public void handleMethodArgumentNotValidExceptionBecauseProductNameIsNull() throws Exception {
        ProductFormDTO body = productForm();
        body.setName(null);
        nameAndDescriptionFieldTest(body, "name", "Nome não pode estar vazio ou ser nulo!");
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por nome de produto vazio")
    public void handleMethodArgumentNotValidExceptionBecauseProductNameIsEmpty() throws Exception {
        ProductFormDTO body = productForm();
        body.setName("");
        nameAndDescriptionFieldTest(body, "name", "Nome não pode estar vazio ou ser nulo!");
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por nome de produto em branco")
    public void handleMethodArgumentNotValidExceptionBecauseProductNameIsBlank() throws Exception {
        ProductFormDTO body = productForm();
        body.setName("");
        nameAndDescriptionFieldTest(body, "name", "Nome não pode estar vazio ou ser nulo!");
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por descrição de produto nulo")
    public void handleMethodArgumentNotValidExceptionBecauseProductDescriptionIsNull() throws Exception {
        ProductFormDTO body = productForm();
        body.setDescription(null);
        nameAndDescriptionFieldTest(body, "description", "Descrição não pode estar vazia ou ser nula!");
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por descrição de produto vazio")
    public void handleMethodArgumentNotValidExceptionBecauseProductDescriptionIsEmpty() throws Exception {
        ProductFormDTO body = productForm();
        body.setDescription("");
        nameAndDescriptionFieldTest(body, "description", "Descrição não pode estar vazia ou ser nula!");
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por descrição de produto em branco")
    public void handleMethodArgumentNotValidExceptionBecauseProductDescriptionIsBlank() throws Exception {
        ProductFormDTO body = productForm();
        body.setDescription("   ");
        nameAndDescriptionFieldTest(body, "description", "Descrição não pode estar vazia ou ser nula!");
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por category_ids com IDs em branco")
    public void handleMethodArgumentNotValidExceptionBecauseCategoryIdsIsBlank() throws Exception {
        List<String> category_ids = new ArrayList<>();
        category_ids.add(new String(""));
        ProductFormDTO body = new ProductFormDTO("Camisa Oficial do Fluminense",
                "A camisa pra você que é tricolor de coraçãol", true, category_ids );
        categoryIdsTest(body,"must not be blank");
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por category_ids com IDs vazio")
    public void handleMethodArgumentNotValidExceptionBecauseCategoryIdsIsEmpty() throws Exception {
        List<String> category_ids = new ArrayList<>();
        category_ids.add(new String("  "));
        ProductFormDTO body = new ProductFormDTO("Camisa Oficial do Fluminense",
                "A camisa pra você que é tricolor de coraçãol", true, category_ids );
        categoryIdsTest(body,"must not be blank");
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por category_ids com IDs nulo")
    public void handleMethodArgumentNotValidExceptionBecauseCategoryIdsIsNull() throws Exception {
        List<String> category_ids = new ArrayList<>();
        category_ids.add(null);
        ProductFormDTO body = new ProductFormDTO("Camisa Oficial do Fluminense",
                "A camisa pra você que é tricolor de coraçãol", true, category_ids );
        categoryIdsTest(body,"must not be blank");
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por lista de category_ids vazia")
    public void handleMethodArgumentNotValidExceptionBecauseListCategoryIdsIsEmpty() throws Exception {
        ProductFormDTO body = new ProductFormDTO("Camisa Oficial do Fluminense",
                "A camisa pra você que é tricolor de coraçãol", true, Collections.EMPTY_LIST );
        categoryIdsTest(body,"must not be empty");
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por lista de category_ids nula")
    public void handleMethodArgumentNotValidExceptionBecauseListCategoryIdsIsNull() throws Exception {
        ProductFormDTO body = new ProductFormDTO("Camisa Oficial do Fluminense",
                "A camisa pra você que é tricolor de coraçãol", true, null);
        categoryIdsTest(body,"must not be empty");
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por lista de category_ids em branco")
    public void handleMethodArgumentNotValidExceptionBecauseListCategoryIdsIsBlank() throws Exception {
        List<String> category_ids = new ArrayList<>();
        ProductFormDTO body = new ProductFormDTO("Camisa Oficial do Fluminense",
                "A camisa pra você que é tricolor de coraçãol", true, category_ids);
        categoryIdsTest(body,"must not be empty");
    }

    @Test
    @DisplayName("Deve retornar status 201 ao salvar produto com atributos corretos")
    public void shouldHaveStatusCreatedWhenSaveProductWithCorretAttributes() throws Exception {
        ProductFormDTO body = productForm();
        Mockito.when(productService.save(body)).thenReturn(productDTO());
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(
                        result -> {
                            ProductDTO productDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                                    ProductDTO.class);
                            assertEquals(productDTO, productDTO());
                        }
                );
    }
}
