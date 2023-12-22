package com.smartcampus.security.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.smartcampus.security.model.CustomUserDetails;

public interface UserRepository extends MongoRepository<CustomUserDetails, String> {
	@Query("{'academicId':?0}")
	Optional<CustomUserDetails> findByAcademicId(String userId);

	@Query("{'email':?0}")
	Optional<CustomUserDetails> findUserByEmail(String email);
	@Query("{'authorities': { $in: ?0 }}")
	List<CustomUserDetails> findAllByAuthorities(List<String> authorities);
}
