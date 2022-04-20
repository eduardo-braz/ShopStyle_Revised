package com.compass.fasttracker.history.DTOs.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VariationDTO implements Serializable {
    private String color;
    private String size;
    private BigDecimal price;
    private Integer quantity;
}
