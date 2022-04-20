package com.compass.ms.DTO;

import com.compass.ms.entity.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PaymentFormDTO {

    @NotNull
    @Enumerated
    private PaymentType type;
    @PositiveOrZero
    @NotNull
    private BigDecimal discount;
    @NotNull
    private boolean status;


}
