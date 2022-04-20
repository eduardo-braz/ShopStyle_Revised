package com.compass.ms.catalog.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VariationFormDTO {

    @NotBlank(message = "Campo Cor não pode estar vazio ou ser nulo!")
    private String color;
    @NotBlank(message = "Campo Tamanho não pode estar vazio ou ser nulo!")
    private String size;
    @NotNull
    @Positive
    private BigDecimal price;
    @NotNull
    @PositiveOrZero
    private Integer quantity;
    @NotBlank(message = "Deve informar qual produto pertence esta categoria.")
    private String product_id;
}
