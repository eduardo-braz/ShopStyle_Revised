package com.compass.ms.catalog.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "products")
public class Product {

    @Id
    private String id;
    private String name;
    private String description;
    private boolean active;
    @DBRef
    private List<Variation> variations = new ArrayList<>();

}
