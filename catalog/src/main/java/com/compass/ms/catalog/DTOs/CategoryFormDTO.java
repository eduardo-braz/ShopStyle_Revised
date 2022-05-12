package com.compass.ms.catalog.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryFormDTO {

    @NotBlank(message = "must not be blank, null or empty")
    private String name;

    @NotNull
    private boolean active;
}
