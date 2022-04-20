package com.compass.ms.catalog.repositories;

import com.compass.ms.catalog.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {

    Optional<Product> findByVariationsIdEquals(String id);

}
