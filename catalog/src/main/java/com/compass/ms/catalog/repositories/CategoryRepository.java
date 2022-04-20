package com.compass.ms.catalog.repositories;

import com.compass.ms.catalog.models.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {




}
