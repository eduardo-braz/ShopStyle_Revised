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
import com.compass.ms.clientFeign.CatalogClient;
import com.compass.ms.clientFeign.CheckoutClient;
import com.compass.ms.clientFeign.CustomerClient;
import com.compass.ms.clientFeign.HistoryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BFFServiceImpl implements BFFService{

    @Autowired
    CatalogClient catalogClient;

    @Autowired
    CheckoutClient checkoutClient;

    @Autowired
    CustomerClient customerClient;

    @Autowired
    HistoryClient historyClient;


    @Override
    public TokenDTO login(LoginFormDTO body) {
        return this.customerClient.login(body);
    }

    @Override
    public UserDTO saveUser(UserFormDTO body){
        return this.customerClient.save(body);
    }

    @Override
    public UserDTO getUser(Long id) {
        return this.customerClient.findById(id);
    }

    @Override
    public UserDTO UpdateUser(UserFormDTO body, Long id) {
        return this.customerClient.update(body, id);
    }

    @Override
    public ProductDTO getProduct(String id) {
        return this.catalogClient.getProduct(id);
    }

    @Override
    public List<ProductDTO> getProductsByCategories(String id) {
        return this.catalogClient.getProductsByCategories(id);
    }

    @Override
    public List<PaymentDTO> getPayments() {
        return this.checkoutClient.getPayments();
    }

    @Override
    public PurchasesDTO savePurchase(PurchasesFormDTO body) {
        return this.checkoutClient.savePurchase(body);
    }

    @Override
    public HistoricDTO getHistoricUser(Long idUser) {
        return this.historyClient.getHistoric(idUser);
    }
}
