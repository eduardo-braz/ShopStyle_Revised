package com.compass.fasttracker.history.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Purchase {

    private Payment paymentMethod;
    private List<Product> products = new ArrayList<>();
}
