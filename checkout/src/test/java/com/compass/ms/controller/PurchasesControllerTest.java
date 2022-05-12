package com.compass.ms.controller;

import com.compass.ms.DTO.PurchasesDTO;
import com.compass.ms.exception.CheckoutExceptionHandler;
import com.compass.ms.service.PaymentService;
import com.compass.ms.service.PurchasesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.compass.ms.entity.Instances.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebMvcTest
@ExtendWith(SpringExtension.class)
@DisplayName("Purchases controller test")
public class PurchasesControllerTest {

    @Autowired
    PurchasesController purchasesController;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PurchasesService purchasesService;

    @MockBean
    PaymentService paymentService;

    @BeforeEach
    public void setup(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(new CheckoutExceptionHandler(), purchasesController).build();
    }

    @Test
    @DisplayName("Deve retornar status 201 ao salvar uma compra")
    public void shouldHaveReturnStatusCreatedWhenSavePurchase() throws Exception {
        when(purchasesService.save(purchasesForm())).thenReturn(purchasesDTO());
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchasesForm())))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect( result -> {
                    PurchasesDTO saved = objectMapper.readValue(result.getResponse().getContentAsString(),
                            PurchasesDTO.class);
                    assertEquals(saved, purchasesDTO());
                } );
    }

}
