package com.compass.ms.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchasesFormDTO {

    @NotNull
    private Long user_id;
    @NotNull
    private Long payment_id;
    @NotNull
    @NotEmpty
    private List<@Valid @NotNull CartFormDTO> cart = new ArrayList<>();
}
