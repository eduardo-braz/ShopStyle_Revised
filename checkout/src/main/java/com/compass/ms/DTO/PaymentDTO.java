package com.compass.ms.DTO;

import com.compass.ms.entity.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PaymentDTO {

    private Long id;
    private PaymentType type;
    private BigDecimal discount;
    private boolean status;
}
