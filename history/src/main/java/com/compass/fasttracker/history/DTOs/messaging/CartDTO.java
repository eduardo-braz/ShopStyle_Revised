package com.compass.fasttracker.history.DTOs.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO implements Serializable {

    private String variant_id;
    private int quantity;
}
