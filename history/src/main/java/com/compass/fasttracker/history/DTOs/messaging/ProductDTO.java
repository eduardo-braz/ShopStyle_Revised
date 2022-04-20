package com.compass.fasttracker.history.DTOs.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO implements Serializable {
    private String name;
    private String description;
    private List<VariationDTO> variations = new ArrayList<>();
}
