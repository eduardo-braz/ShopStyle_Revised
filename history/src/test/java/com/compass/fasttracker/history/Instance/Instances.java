package com.compass.fasttracker.history.Instance;

import com.compass.fasttracker.history.DTOs.*;
import com.compass.fasttracker.history.DTOs.Enum.PaymentType;
import com.compass.fasttracker.history.DTOs.Enum.Sex;
import com.compass.fasttracker.history.DTOs.messaging.CartDTO;
import com.compass.fasttracker.history.DTOs.messaging.ProductDTO;
import com.compass.fasttracker.history.DTOs.messaging.PurchasesDTO;
import com.compass.fasttracker.history.DTOs.messaging.VariationDTO;
import com.compass.fasttracker.history.models.Historic;
import javafx.util.converter.LocalDateStringConverter;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Instances {

    public static User user(){
        User user = new User();
        user.setFirstName("Maria");
        user.setLastName("Oliveira");
        user.setSex(Sex.Feminino);
        user.setCpf("310.119.950-69");
        LocalDate birthDate = new LocalDateStringConverter().fromString("25/12/2020");
        user.setBirthdate(birthDate);
        user.setEmail("mariater@email.com");
        return user;
    }

    public static List<Product> productList(){
        List<Product> products = new ArrayList<>();
        products.add(new Product("Camisa Oficial do Fluminense", "A camisa pra voce que eh tricolor",
                "Tricolor", "G", BigDecimal.valueOf(249.99), 1  ));
        products.add(new Product("Camisa Branca", "A camisa ideal para o dia a dia",
                "Branca", "M", BigDecimal.valueOf(49.99), 2  ));
        return products;
    }

    public static Payment payment(){
        return new Payment(PaymentType.CREDIT_CARD, BigDecimal.valueOf(2), true);
    }

    public static List<Purchase> purchaseList(){
        List<Purchase> purchaseList = new ArrayList<>();
        Purchase purchase = new Purchase(payment(), productList());
        purchaseList.add(purchase);
        return purchaseList;
    }

    public static Historic historic(){
        return new Historic("6255d5fb5afde22e11f9e318", user(), purchaseList(),
                BigDecimal.valueOf(349.97), new LocalDateStringConverter().fromString("09/05/2022"));
    }

    public static HistoricDTO historicDTO(){
        return new ModelMapper().map(historic(), HistoricDTO.class);
    }

    public static PurchasesDTO purchasesDTO(){
        List<CartDTO> cart = new ArrayList<>();
        cart.add(new CartDTO("624f405d3b9bc442d1e0b037", 2));
        cart.add(new CartDTO("624f405d3b9bc442d1e0b051", 1));
        return new PurchasesDTO(859L, 53L, cart, BigDecimal.valueOf(349.97),
                new LocalDateStringConverter().fromString("09/05/2022"));
    }

    public static ProductDTO productDTO(){
        List<VariationDTO> variations = new ArrayList<>();
        variations.add(new VariationDTO("Vermelha", "GG", BigDecimal.valueOf(249.90), 10));
        variations.add(new VariationDTO("Tricolor", "G", BigDecimal.valueOf(199.90), 15));
        return new ProductDTO("Camisa Oficial do Fluminense", "A camisa pra voce que eh tricolor",
                variations);
    }

}
