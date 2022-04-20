package com.compass.ms.catalog.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductFormDTO {

    @NotBlank(message = "Nome não pode estar vazio ou ser nulo!")
    private String name;
    @NotBlank(message = "Descrição não pode estar vazia ou ser nula!")
    private String description;
    @NotNull
    private boolean active;
    @NotEmpty
    private List<@NotBlank String> category_ids = new ArrayList<>();

}
