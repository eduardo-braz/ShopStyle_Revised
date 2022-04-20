package com.compass.ms.DTO;

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
public class PurchasesDTO implements Serializable {

    private Long id;
    private Long user_id;
    private Long payment_id;
    private List<CartDTO> cart = new ArrayList<>();
    private BigDecimal price;
}
