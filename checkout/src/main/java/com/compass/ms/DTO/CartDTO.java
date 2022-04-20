package com.compass.ms.DTO;

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
