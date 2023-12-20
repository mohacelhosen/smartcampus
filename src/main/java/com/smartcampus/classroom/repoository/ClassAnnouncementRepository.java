package com.smartcampus.classroom.repoository;

import com.smartcampus.classroom.model.ClassAnnouncement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ClassAnnouncementRepository extends MongoRepository<ClassAnnouncement, String> {
    @Query("{'departmentCode': ?0, 'semesterNumber': ?1}")
    List<ClassAnnouncement> findAllByDepartmentCodeAndSemesterNumber(String departmentCode, Integer semesterNumber);
}
