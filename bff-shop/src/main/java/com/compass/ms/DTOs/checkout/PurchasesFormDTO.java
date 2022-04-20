package com.compass.ms.DTOs.checkout;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchasesFormDTO implements Serializable {

    private Long user_id;
    private Long payment_id;
    private List<CartDTO> cart = new ArrayList<>();
}
