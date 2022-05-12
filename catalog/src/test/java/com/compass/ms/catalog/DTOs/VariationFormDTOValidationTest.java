package com.compass.ms.catalog.DTOs;

import com.compass.ms.catalog.controllers.VariationController;
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

import java.math.BigDecimal;

import static com.compass.ms.catalog.models.Instances.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@DisplayName("Testes de validação de requisição (Body)")
public class VariationFormDTOValidationTest {

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
    VariationController variationController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CatalogExceptionHandler(), variationController).build();
    }

    private void attributesFieldsTest(VariationFormDTO body, String field, String message) throws Exception{
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/variations")
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

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por \"color\" nulo")
    public void handleMethodArgumentNotValidExceptionBecauseColorIsNull() throws Exception {
        VariationFormDTO body = variationForm();
        body.setColor(null);
        attributesFieldsTest(body, "color", "Campo Cor não pode estar vazio ou ser nulo!");
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por \"color\" vazio")
    public void handleMethodArgumentNotValidExceptionBecauseColorIsEmpty() throws Exception {
        VariationFormDTO body = variationForm();
        body.setColor("");
        attributesFieldsTest(body, "color", "Campo Cor não pode estar vazio ou ser nulo!");
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por \"color\" em branco")
    public void handleMethodArgumentNotValidExceptionBecauseColorIsBlank() throws Exception {
        VariationFormDTO body = variationForm();
        body.setColor("      ");
        attributesFieldsTest(body, "color", "Campo Cor não pode estar vazio ou ser nulo!");
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por \"size\" nulo")
    public void handleMethodArgumentNotValidExceptionBecauseSizeIsNull() throws Exception {
        VariationFormDTO body = variationForm();
        body.setSize(null);
        attributesFieldsTest(body, "size", "Campo Tamanho não pode estar vazio ou ser nulo!");
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por \"size\" vazio")
    public void handleMethodArgumentNotValidExceptionBecauseSizeIsEmpty() throws Exception {
        VariationFormDTO body = variationForm();
        body.setSize("");
        attributesFieldsTest(body, "size", "Campo Tamanho não pode estar vazio ou ser nulo!");
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por \"size\" em branco")
    public void handleMethodArgumentNotValidExceptionBecauseSizeIsBlank() throws Exception {
        VariationFormDTO body = variationForm();
        body.setSize("      ");
        attributesFieldsTest(body, "size", "Campo Tamanho não pode estar vazio ou ser nulo!");
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por \"product_id\" nulo")
    public void handleMethodArgumentNotValidExceptionBecauseProdutctIdIsNull() throws Exception {
        VariationFormDTO body = variationForm();
        body.setProduct_id(null);
        attributesFieldsTest(body, "product_id", "Deve informar qual produto pertence esta categoria.");
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por \"product_id\" vazio")
    public void handleMethodArgumentNotValidExceptionBecauseProdutctIdIsEmpty() throws Exception {
        VariationFormDTO body = variationForm();
        body.setProduct_id("");
        attributesFieldsTest(body, "product_id", "Deve informar qual produto pertence esta categoria.");
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por \"product_id\" em branco")
    public void handleMethodArgumentNotValidExceptionBecauseProdutctIdIsBlank() throws Exception {
        VariationFormDTO body = variationForm();
        body.setProduct_id("      ");
        attributesFieldsTest(body, "product_id", "Deve informar qual produto pertence esta categoria.");
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por \"quantity\" null")
    public void handleMethodArgumentNotValidExceptionBecauseQuantityIsNull() throws Exception {
        VariationFormDTO body = variationForm();
        body.setQuantity(null);
        attributesFieldsTest(body, "quantity", "must not be null");
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por \"quantity\" menor que zero")
    public void handleMethodArgumentNotValidExceptionBecauseQuantityLessThanZero() throws Exception {
        VariationFormDTO body = variationForm();
        body.setQuantity(-1);
        attributesFieldsTest(body, "quantity", "must be greater than or equal to 0");
    }

    @Test
    @DisplayName("Deve retornar status 201 ao salvar variation com \"quantity\" igual zero")
    public void shouldHaveStatusCreatedWhenSaveVariationWithQuantityEqualZero() throws Exception, InvalidOperationException {
        VariationFormDTO body = variationForm();
        body.setQuantity(0);
        Mockito.when(variationService.save(body)).thenReturn(variationDTO());
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/variations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(
                        result -> {
                            VariationDTO variationDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                                    VariationDTO.class);
                            assertEquals(variationDTO, variationDTO());
                        }
                );
    }


    @Test
    @DisplayName("Lança MethodArgumentNotValidException por \"price\" menor que zero")
    public void handleMethodArgumentNotValidExceptionBecausePriceLessThanZero() throws Exception {
        VariationFormDTO body = variationForm();
        body.setPrice(BigDecimal.valueOf(-1L));
        attributesFieldsTest(body, "price", "must be greater than 0");
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por \"price\" igual zero")
    public void handleMethodArgumentNotValidExceptionBecausePriceZero() throws Exception {
        VariationFormDTO body = variationForm();
        body.setPrice(BigDecimal.ZERO);
        attributesFieldsTest(body, "price", "must be greater than 0");
    }

    @Test
    @DisplayName("Lança MethodArgumentNotValidException por \"price\" nulo")
    public void handleMethodArgumentNotValidExceptionBecausePriceNull() throws Exception {
        VariationFormDTO body = variationForm();
        body.setPrice(null);
        attributesFieldsTest(body, "price", "must not be null");
    }

    @Test
    @DisplayName("Deve retornar status 201 ao salvar variation com atributos corretos")
    public void shouldHaveStatusCreatedWhenSaveVariationWithCorretAttributes() throws Exception, InvalidOperationException {
        VariationFormDTO body = variationForm();
        Mockito.when(variationService.save(body)).thenReturn(variationDTO());
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/variations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(
                        result -> {
                            VariationDTO variationDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                                    VariationDTO.class);
                            assertEquals(variationDTO, variationDTO());
                        }
                );
    }

}
