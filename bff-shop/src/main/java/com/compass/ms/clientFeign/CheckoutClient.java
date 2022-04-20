package com.compass.ms.clientFeign;

import com.compass.ms.DTOs.checkout.PaymentDTO;
import com.compass.ms.DTOs.checkout.PurchasesDTO;
import com.compass.ms.DTOs.checkout.PurchasesFormDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("checkout")
public interface CheckoutClient {


    @RequestMapping(value = "/v1/payments", method = RequestMethod.GET)
    List<PaymentDTO> getPayments();

    @RequestMapping(value = "/v1/purchases", method = RequestMethod.POST)
    PurchasesDTO savePurchase(@RequestBody PurchasesFormDTO body);
}
