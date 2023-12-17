package com.smartcampus.department.service;

import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.course.model.Course;
import com.smartcampus.course.service.CourseService;
import com.smartcampus.department.model.Department;
import com.smartcampus.department.repository.DepartmentRepository;
import com.smartcampus.exception.IncompleteDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.List;

@Service
public class DepartmentServie {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private CourseService courseService;
    private static final Logger logger = LoggerFactory.getLogger(DepartmentServie.class);

    public Department registerDepartment(Department department){
        if (valid(department)) {
            department.setCreatedDateTime(new ModelLocalDateTime(null));
            return departmentRepository.save(department);
        } else {
            throw new IncompleteDataException("Incomplete department information or duplicate courseCode not allowed to register");
        }
    }

    private boolean valid(Department department) {
        return department != null && department.getDepartmentName() != null && !department.getDepartmentName().isEmpty()
                && department.getDepartmentShortName() != null && !department.getDepartmentShortName().isEmpty()
                && department.getDepartmentCode() != null && !department.getDepartmentCode().isEmpty()
                && !codeExists(department.getDepartmentCode());
    }

    private boolean codeExists(String code) {
        return departmentRepository.findByDepartmentCode(code).isPresent();
    }


}
