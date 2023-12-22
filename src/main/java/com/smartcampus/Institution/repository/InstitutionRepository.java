package com.smartcampus.Institution.repository;

import com.smartcampus.Institution.model.Institution;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface InstitutionRepository extends MongoRepository<Institution, String> {
    @Query("{'institutionCode': ?0}")
    Optional<Institution> findByInstitutionCode(String institutionCode);
}
