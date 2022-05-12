package com.compass.ms.catalog.models;

import com.compass.ms.catalog.DTOs.*;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Instances {

    public static Variation variationOne(){
        return new Variation("624f405d3b9bc442d1e0b037", "Tricolor", "M",
                BigDecimal.valueOf(199.90), 6);
    }

    public static Variation variationTwo(){
        return new Variation("624f407e3b9bc442d1e0b038", "Vermelha", "GG",
                BigDecimal.valueOf(249.90), 10);
    }

    public static Variation variationThree(){
        return new Variation("624f41c53b9bc442d1e0b03b", "Branca", "G",
                BigDecimal.valueOf(29.90), 15);
    }

    public static Product productOne(){
        List<Variation> variationList = new ArrayList<>();
        variationList.add(variationOne());
        variationList.add(variationTwo());
        return new Product("624f40363b9bc442d1e0b036", "Camisa Oficial do Fluminense",
                "A camisa pra voce que eh tricolor", true,  variationList);
    }

    public static Product productTwo(){
        List<Variation> variations = new ArrayList<>();
        variations.add(variationThree());
        return new Product("624f41a63b9bc442d1e0b03a", "Camisa Branca",
                "A camisa ideal para o dia a dia", true, variations);
    }

    public static Category category(){
        List<Product> products = new ArrayList<>();
        products.add(productOne());
        products.add(productTwo());
        return new Category("624f3fe23b9bc442d1e0b034", "Camisas", true, products);
    }

    public static VariationDTO variationDTO(){
        return new ModelMapper().map(variationOne(), VariationDTO.class);
    }

    public static VariationFormDTO variationForm(){
        VariationFormDTO formDTO = new ModelMapper().map(variationOne(), VariationFormDTO.class);
        formDTO.setProduct_id("624f40363b9bc442d1e0b036");
        return formDTO;
    }

    public static ProductFormDTO productForm(){
        List<String> category_ids = new ArrayList<>();
        category_ids.add("624f3fe23b9bc442d1e0b034");
        return new ProductFormDTO("Camisa Oficial do Fluminense",
                "A camisa pra você que é tricolor de coraçãol", true, category_ids );
    }

    public static ProductDTO productDTO(){
        return new ModelMapper().map(productOne(), ProductDTO.class);
    }

    public static CategoryFormDTO categoryForm(){
        return new ModelMapper().map(category(), CategoryFormDTO.class);
    }

    public static CategoryDTO categoryDTO(){
        return new ModelMapper().map(category(), CategoryDTO.class);
    }

    public static CartDTO cartDTO(){
        return new CartDTO(variationOne().getId(), 2);
    }

    public static List<CartDTO> cartDTOList(){
        List<CartDTO> cartDTOList = new ArrayList<>();
        cartDTOList.add(new CartDTO(variationOne().getId(), 2));
        cartDTOList.add(new CartDTO(variationTwo().getId(), 1));
        return cartDTOList;
    }

    public static List<ProductDTO> productDTOList(){
        List<ProductDTO> productDTOList = new ArrayList<>();
        productDTOList.add(productDTO());
        return productDTOList;
    }
    public static Category categoryTwo(){
        List<Product> products = new ArrayList<>();
        return new Category("624f3fe23b9bc442d1e0b038", "Bermudas", true, products);
    }
}
