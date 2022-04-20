package com.compass.fasttracker.history.repository;

import com.compass.fasttracker.history.models.Historic;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HistoryRepository extends MongoRepository<Historic, String> {

    public List<Historic> findByUserId(Long id);
}
