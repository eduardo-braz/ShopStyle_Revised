package com.compass.ms.catalog.repositories;

import com.compass.ms.catalog.models.Variation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VariationRepository extends MongoRepository<Variation, String> {
}
