package com.smartcampus.library.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.smartcampus.library.model.LibraryCardInformation;

public interface LibraryCardInformationRepo extends MongoRepository<LibraryCardInformation, String> {
	@Query("{'academicId':?0}")
	Optional<LibraryCardInformation> findByAcademicId(String acadmicId);
}
