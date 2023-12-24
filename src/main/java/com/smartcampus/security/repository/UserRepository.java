package com.smartcampus.security.repository;


import com.smartcampus.security.model.CustomUserDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<CustomUserDetails, String> {
	@Query("{'academicId':?0}")
	Optional<CustomUserDetails> findByAcademicId(String academicId);

	@Query("{'email':?0}")
	Optional<CustomUserDetails> findByEmail(String email);
	@Query("{'authorities': { $in: ?0 }, 'institutionCode': ?1}")
	List<CustomUserDetails> findAllByAuthorities(List<String> authorities, String institutionCode);

}
