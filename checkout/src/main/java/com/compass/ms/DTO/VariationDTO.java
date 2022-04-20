package com.compass.ms.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VariationDTO {

    private String id;
    private String color;
    private String size;
    private BigDecimal price;
    private Integer quantity;
}
