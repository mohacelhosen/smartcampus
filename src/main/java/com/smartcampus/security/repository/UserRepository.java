package com.smartcampus.security.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.smartcampus.security.model.CustomUserDetails;

public interface UserRepository extends MongoRepository<CustomUserDetails, String> {
	@Query("{'userId':?0}")
	Optional<CustomUserDetails> findByUserId(String userId);

	@Query("{'email':?0}")
	Optional<CustomUserDetails> findUserByEmail(String email);
}
