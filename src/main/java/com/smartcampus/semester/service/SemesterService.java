package com.smartcampus.semester.service;

import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.exception.AlreadyExistsException;
import com.smartcampus.semester.model.Semester;
import com.smartcampus.semester.repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SemesterService {

    @Autowired
    private SemesterRepository semesterRepository;

    public Semester registerSemester(Semester semester){
        if (valid(semester)){
            semester.setCreatedDateTime(new ModelLocalDateTime(null));
            semester.setCreatedBy(null);
            return semesterRepository.save(semester);
        }else{
            throw new AlreadyExistsException("Department and Semester already Registered");
        }
    }

    public Semester addCourseAndTeacherAndClass(String courseId, String teacherId, String classJoinCode){
       return null;
    }

    private boolean valid(Semester semester) {
        return semester != null && semester.getDepartmentName() != null && !semester.getDepartmentName().isEmpty()
                && semester.getSemesterNumber() != null && semester.getSemesterNumber()>0
                && semester.getDepartmentCode() != null && !semester.getDepartmentCode().isEmpty() && !departmentAndSemesterExist(semester.getDepartmentCode(), semester.getSemesterNumber());
    }

    private boolean codeExists(String code) {
        return semesterRepository.findBySemesterCode(code).isPresent();
    }
    private boolean departmentAndSemesterExist(String departmentCode, Integer semesterNumber) {
        return   semesterRepository.findByDepartmentCodeAndSemesterNumber(departmentCode, semesterNumber).isPresent();
    }

    private String generateSemesterCode(){
        return String.valueOf(UUID.randomUUID().getMostSignificantBits()).replace("-","");
    }
}
