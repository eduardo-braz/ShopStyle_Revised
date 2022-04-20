package com.compass.fasttracker.history.DTOs;

import com.compass.fasttracker.history.DTOs.Enum.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Payment {

    private PaymentType type;
    private BigDecimal discount;
    private boolean status;
}
