package com.smartcampus.filemngt.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.smartcampus.filemngt.model.Photo;

public interface PhotoRepository extends MongoRepository<Photo, String> {
}
