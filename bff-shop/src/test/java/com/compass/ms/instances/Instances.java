package com.compass.ms.instances;

import com.compass.ms.DTOs.LoginFormDTO;
import com.compass.ms.DTOs.TokenDTO;
import com.compass.ms.DTOs.catalog.ProductDTO;
import com.compass.ms.DTOs.catalog.Variation;
import com.compass.ms.DTOs.checkout.*;
import com.compass.ms.DTOs.customer.Sex;
import com.compass.ms.DTOs.customer.User;
import com.compass.ms.DTOs.customer.UserDTO;
import com.compass.ms.DTOs.customer.UserFormDTO;
import com.compass.ms.DTOs.history.*;
import javafx.util.converter.LocalDateStringConverter;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Instances {

    public static TokenDTO tokenDTO(){
        return new TokenDTO("eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJTaG9wIFN0eWxlIiwic3ViIjoiMjgiLCJpYXQiOjE2NTA0NTg5MT" +
                "csImV4cCI6MTY1MDQ2MjUxN30.G9fcc7pY_w9MCEKPHuLBsAc55gaQ8XI1gnUt1iowJEM",
                "Bearer");
    }

    public static LoginFormDTO login(){
        return new LoginFormDTO(user().getEmail(), "12345678");
    }

    public static User user(){
        User user = new User();
        user.setId(53L);
        user.setFirstName("Maria");
        user.setLastName("Oliveira");
        user.setSex(Sex.Feminino);
        user.setCpf("310.119.950-69");
        LocalDate birthDate = new LocalDateStringConverter().fromString("25/12/2020");
        user.setBirthdate(birthDate);
        user.setEmail("mariater@email.com");
        return user;
    }

    public static UserFormDTO userform(){
        return new ModelMapper().map(user(), UserFormDTO.class);
    }

    public static UserDTO userDTO(){
        return new ModelMapper().map(user(), UserDTO.class);
    }

    public static ProductDTO productDTO(){
        List<Variation> variations = new ArrayList<>();
        variations.add(new Variation("624f405d3b9bc442d1e0b037","Vermelha", "GG",
                BigDecimal.valueOf(249.90), 10));
        variations.add(new Variation("624f405d3b9bc442d1e0b059","Tricolor", "G",
                BigDecimal.valueOf(199.90), 15));
        return new ProductDTO("624f40363b9bc442d1e0b036", "Camisa Oficial do Fluminense",
                "A camisa pra voce que eh tricolor", true, variations);
    }

    public static Variation variationOne(){
        return new Variation("624f405d3b9bc442d1e0b037","Vermelha", "GG",
                BigDecimal.valueOf(249.90), 10);
    }

    public static PaymentDTO paymentDTO(){
        return new PaymentDTO(175L, PaymentType.CREDIT_CARD, BigDecimal.valueOf(2), true);
    }

    public static PurchasesFormDTO purchasesForm(){
        List<CartDTO> cart = new ArrayList<>();
        cart.add(new CartDTO("624f405d3b9bc442d1e0b037", 2));
        cart.add(new CartDTO("624f405d3b9bc442d1e0b059", 1));
        return new PurchasesFormDTO(53L, 175L, cart);
    }

    public static PurchasesDTO purchasesDTO(){
        List<CartDTO> cart = new ArrayList<>();
        cart.add(new CartDTO("624f405d3b9bc442d1e0b037", 2));
        cart.add(new CartDTO("624f405d3b9bc442d1e0b051", 1));
        return new PurchasesDTO(5432L, 53L, 175L, cart, BigDecimal.valueOf(349.97));
    }

    public static HistoricDTO historicDTO(){
        List<PurchaseHistory> historic = new ArrayList<>();
        historic.add(purchaseHistory());
        return new HistoricDTO(
                new ModelMapper().map(user(), UserHistory.class), historic, BigDecimal.valueOf(349.97),
                new LocalDateStringConverter().fromString("09/05/2022")
        );
    }

    public static ProductHistory productHistory(){
        return new ProductHistory("Camisa Oficial do Fluminense", "A camisa pra voce que eh tricolor", "Tricolor",
                "G", BigDecimal.valueOf(199.90), 1);
    }

    public static PurchaseHistory purchaseHistory(){
        PaymentHistory paymentHistory = new PaymentHistory(PaymentType.CREDIT_CARD, BigDecimal.valueOf(2), true);
        List<ProductHistory> productHistories = new ArrayList<>();
        productHistories.add(productHistory());
        return new PurchaseHistory(paymentHistory, productHistories);
    }

    public static UserHistory userHistory(){
        return new ModelMapper().map(user(), UserHistory.class);
    }
}
