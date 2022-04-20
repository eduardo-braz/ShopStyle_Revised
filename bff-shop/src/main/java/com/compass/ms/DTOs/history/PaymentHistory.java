package com.compass.ms.DTOs.history;

import com.compass.ms.DTOs.checkout.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PaymentHistory {

    private PaymentType type;
    private BigDecimal discount;
    private boolean status;
}
