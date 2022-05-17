package com.compass.ms.controller;

import com.compass.ms.DTOs.LoginFormDTO;
import com.compass.ms.DTOs.TokenDTO;
import com.compass.ms.DTOs.catalog.ProductDTO;
import com.compass.ms.DTOs.checkout.PaymentDTO;
import com.compass.ms.DTOs.checkout.PurchasesDTO;
import com.compass.ms.DTOs.checkout.PurchasesFormDTO;
import com.compass.ms.DTOs.customer.UserDTO;
import com.compass.ms.DTOs.customer.UserFormDTO;
import com.compass.ms.DTOs.history.HistoricDTO;
import com.compass.ms.clientFeign.CatalogClient;
import com.compass.ms.clientFeign.CheckoutClient;
import com.compass.ms.clientFeign.CustomerClient;
import com.compass.ms.clientFeign.HistoryClient;
import com.compass.ms.exception.BFFExceptionHandler;
import com.compass.ms.instances.Instances;
import com.compass.ms.security.SecurityConfiguration;
import com.compass.ms.services.BFFService;
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
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static com.compass.ms.instances.Instances.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@DisplayName("BFF Controller Test")
public class BFFControllerTest {

    @MockBean
    BFFService bffService;

    @MockBean
    SecurityConfiguration securityConfiguration;

    @MockBean
    WebSecurityConfiguration webSecurityConfiguration;

    @Autowired
    BFFController bffController;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new BFFExceptionHandler(), bffController).build();
    }

    @Test
    public void shouldHaveReturnStatus200AndTokenDtoWhenSucessfullLogin() throws Exception {
        LoginFormDTO body = new LoginFormDTO(login().getEmail(), login().getPassword());
        when(bffService.login(any(LoginFormDTO.class))).thenReturn(tokenDTO());
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        result -> {
                            TokenDTO tokenDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                                    TokenDTO.class);
                            assertEquals(tokenDTO(), tokenDTO);
                        }
                );
    }

    @Test
    public void shouldHaveReturnStatus200AndUserDtoWhenSucessfullSaveUser() throws Exception {
        UserFormDTO body = userform();
        when(bffService.saveUser(any(UserFormDTO.class))).thenReturn(userDTO());
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(
                        result -> {
                            UserDTO userDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                                    UserDTO.class);
                            assertEquals(userDTO(), userDTO);
                        }
                );
    }

    @Test
    public void shouldHaveReturnStatus200AndUserDtoWhenGetUser() throws Exception {
        when(bffService.getUser(anyLong())).thenReturn(userDTO());
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/v1/users/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        result -> {
                            UserDTO userDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                                    UserDTO.class);
                            assertEquals(userDTO(), userDTO);
                        }
                );
    }

    @Test
    public void shouldHaveReturnStatus200AndUserDtoWhenSucessfullUpdateUser() throws Exception {
        UserFormDTO body = userform();
        when(bffService.UpdateUser(any(UserFormDTO.class), anyLong())).thenReturn(userDTO());
        mockMvc.perform( MockMvcRequestBuilders
                        .put("/v1/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        result -> {
                            UserDTO userDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                                    UserDTO.class);
                            assertEquals(userDTO(), userDTO);
                        }
                );
    }

    @Test
    public void shouldHaveReturnStatus200AndProductDtoWhenGetProduct() throws Exception {
        when(bffService.getProduct(anyString())).thenReturn(productDTO());
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/v1/products/{id}", "abcd"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        result -> {
                            ProductDTO productDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                                    ProductDTO.class);
                            assertEquals(productDTO(), productDTO);
                        }
                );
    }

    @Test
    public void shouldHaveReturnStatus200AndListOfProductDtoWhenGetProductsByCategory() throws Exception {
        List<ProductDTO> productDTOS = new ArrayList<>();
        productDTOS.add(productDTO());
        when(bffService.getProductsByCategories(anyString())).thenReturn(productDTOS);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/v1/categories/{id}/products", "abcd"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        result -> {
                            List<ProductDTO> productDTOReceived = objectMapper.readValue(result.getResponse()
                                            .getContentAsString(),
                                    ArrayList.class);
                            assertTrue(productDTOReceived.toString().contains(productDTOS.get(0).getName()));
                            assertTrue(productDTOReceived.toString().contains(productDTOS.get(0).getId()));
                            assertTrue(productDTOReceived.toString().contains(productDTOS.get(0).getDescription()));
                            assertTrue(productDTOReceived.toString().contains(productDTOS.get(0).getVariations()
                                    .get(0).getId()));
                            assertTrue(productDTOReceived.toString().contains(productDTOS.get(0).getVariations()
                                    .get(0).getColor()));
                            assertTrue(productDTOReceived.toString().contains(productDTOS.get(0).getVariations()
                                    .get(0).getSize()));
                            assertTrue(productDTOReceived.toString().contains(productDTOS.get(0).getVariations()
                                    .get(0).getQuantity().toString()));
                            assertTrue(productDTOReceived.toString().contains(productDTOS.get(0).getVariations()
                                    .get(0).getPrice().toString()));
                        }
                );
    }

    @Test
    public void shouldHaveReturnStatus200AndListOfPaymentDtoWhenGetPayment() throws Exception {
        List<PaymentDTO> paymentDTOS = new ArrayList<>();
        paymentDTOS.add(paymentDTO());
        when(bffService.getPayments()).thenReturn(paymentDTOS);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/v1/payments"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        result -> {
                            List<PaymentDTO> paymentReceived = objectMapper.readValue(result.getResponse()
                                            .getContentAsString(),
                                    ArrayList.class);
                            assertTrue(paymentReceived.toString().contains(paymentDTOS.get(0).getId().toString()));
                            assertTrue(paymentReceived.toString().contains(paymentDTOS.get(0).getDiscount().toString()));
                            assertTrue(paymentReceived.toString().contains(paymentDTOS.get(0).getType().toString()));
                        }
                );
    }

    @Test
    public void shouldHaveReturnStatus201AndPurchasesDtoWhenSavePurchases() throws Exception {
        PurchasesFormDTO body = purchasesForm();
        when(bffService.savePurchase(any(PurchasesFormDTO.class))).thenReturn(purchasesDTO());
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(
                        result -> {
                            PurchasesDTO purchasesDTO = objectMapper.readValue(result.getResponse()
                                            .getContentAsString(), PurchasesDTO.class);
                            assertEquals(purchasesDTO(), purchasesDTO);
                            }
                );
    }

    @Test
    public void shouldHaveReturnStatus200AndHistoricDtoWhenGetHistoricUser() throws Exception {
        when(bffService.getHistoricUser(anyLong())).thenReturn(historicDTO());
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/v1/historic/user/{idUser}",1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        result -> {
                            HistoricDTO historicDTO = objectMapper.readValue(result.getResponse()
                                            .getContentAsString(), HistoricDTO.class);
                            assertEquals(historicDTO(), historicDTO);
                        }
                );
    }

}
