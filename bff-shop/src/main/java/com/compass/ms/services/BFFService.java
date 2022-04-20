package com.compass.ms.services;

import com.compass.ms.DTOs.LoginFormDTO;
import com.compass.ms.DTOs.TokenDTO;
import com.compass.ms.DTOs.catalog.ProductDTO;
import com.compass.ms.DTOs.checkout.PaymentDTO;
import com.compass.ms.DTOs.checkout.PurchasesDTO;
import com.compass.ms.DTOs.checkout.PurchasesFormDTO;
import com.compass.ms.DTOs.customer.UserDTO;
import com.compass.ms.DTOs.customer.UserFormDTO;
import com.compass.ms.DTOs.history.HistoricDTO;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface BFFService {

    TokenDTO login(LoginFormDTO body);

    UserDTO saveUser(UserFormDTO body);

    UserDTO getUser(Long id);

    UserDTO UpdateUser(UserFormDTO body, Long id);

    ProductDTO getProduct(String id);

    List<ProductDTO> getProductsByCategories(String id);

    List<PaymentDTO> getPayments();

    PurchasesDTO savePurchase(PurchasesFormDTO body);

    HistoricDTO getHistoricUser(Long idUser);
}
