package com.compass.ms.DTOs.history;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseHistory {

    private PaymentHistory paymentMethod;
    private List<ProductHistory> products = new ArrayList<>();
}
