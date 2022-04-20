package com.compass.ms.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartFormDTO {

    @NotNull
    private String variant_id;
    @NotNull
    @Positive
    private int quantity;
}
