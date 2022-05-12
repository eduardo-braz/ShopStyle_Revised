package com.compass.ms.entity;

import com.compass.ms.DTO.*;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Instances {

    public static Payment payment(){
        return new Payment(1L, PaymentType.DEBIT, BigDecimal.valueOf(10), true);
    }

    public static PaymentDTO paymentDTO(){
        return new ModelMapper().map(payment(), PaymentDTO.class);
    }

    public static PaymentFormDTO paymentForm(){
        return new ModelMapper().map(payment(), PaymentFormDTO.class);
    }

    public static Cart cart(){
        return new Cart(101L, "624f41c53b9bc442d1e0b03b", 10);
    }

    public static List<Cart> cartList(){
        List<Cart> cartList = new ArrayList<>();
        cartList.add(new Cart(101L, "624f41c53b9bc442d1e0b03b", 3));
        cartList.add(new Cart(102L, "624f405d3b9bc442d1e0b037", 1));
        return cartList;
    }

    public static Purchases purchases(){
        return new Purchases(1L,28L,53L,cartList(), BigDecimal.valueOf(349.40));
    }

    public static PurchasesDTO purchasesDTO(){
        return new ModelMapper().map(purchases(),PurchasesDTO.class);
    }

    public static PurchasesFormDTO purchasesForm(){
        List<CartFormDTO> cartForm = new ArrayList<>();
        cartList().forEach( item -> {
            cartForm.add(new ModelMapper().map(item, CartFormDTO.class));
        });
        return new PurchasesFormDTO(28L,53L, cartForm);
    }

    public static ProductDTO firstProductDTO() {
        return new ProductDTO("624f40363b9bc442d1e0b036",
                "Camisa Oficial do Fluminense",
                "A camisa para você que é tricolor de coração",
                true, firstVariationList());
    }

    public static List<VariationDTO> firstVariationList(){
        List<VariationDTO> variations = new ArrayList<>();
        variations.add(new VariationDTO("624f405d3b9bc442d1e0f000",
                "Tricolor", "M", BigDecimal.valueOf(199.90),18));
        variations.add(new VariationDTO("624f41c53b9bc442d1e0b03b",
                "Branca", "GG", BigDecimal.valueOf(29.90),15));
        return variations;
    }

    public static ProductDTO secondProductDTO() {
        List<VariationDTO> variations = new ArrayList<>();
        variations.add(new VariationDTO("624f405d3b9bc442d1e0b037",
                "Branca", "M", BigDecimal.valueOf(199.90),18));
        return new ProductDTO("624f40363b9bc442d1e0ba81",
                "Camisa de malha",
                "A camisa para o dia a dia",
                true, variations);
    }

}
