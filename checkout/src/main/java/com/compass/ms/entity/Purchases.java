package com.compass.ms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "purchases")
public class Purchases {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
        private Long user_id;
        private Long payment_id;
        @OneToMany
        private List<Cart> cart = new ArrayList<>();
        private BigDecimal price = BigDecimal.ZERO;

}
