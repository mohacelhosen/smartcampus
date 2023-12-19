package com.smartcampus.department.service;

import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.course.model.Course;
import com.smartcampus.course.service.CourseService;
import com.smartcampus.department.model.Department;
import com.smartcampus.department.repository.DepartmentRepository;
import com.smartcampus.exception.AlreadyExistsException;
import com.smartcampus.exception.IncompleteDataException;
import com.smartcampus.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServie {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private CourseService courseService;
    private static final Logger logger = LoggerFactory.getLogger(DepartmentServie.class);

    @Transactional
    public Department registerDepartment(Department department) {
        if (valid(department)) {
            department.setCreatedDateTime(new ModelLocalDateTime(null));
            department.setCourseList(null);
            return departmentRepository.save(department);
        } else {
            throw new IncompleteDataException("Incomplete department information or duplicate courseCode not allowed to register");
        }
    }

    @Transactional
    public Department addCourseInDepartment(String departmentCode, String courseCode) {
        Optional<Department> optionalDepartment = departmentRepository.findByDepartmentCode(departmentCode);

        if (optionalDepartment.isEmpty()) {
            throw new NotFoundException("Invalid department code: " + departmentCode);
        }

        Course dbCourse = courseService.findByCourseCode(courseCode);
        if (dbCourse == null) {
            throw new NotFoundException("Attempted to add a non-registered course to the department");
        }

        Department dbDepartment = optionalDepartment.get();

        List<Course> courseList = dbDepartment.getCourseList();
        if (courseList == null ||courseList.isEmpty()) {
            courseList = new ArrayList<>();
            dbDepartment.setCourseList(courseList);
        } else {
            boolean alreadyPresent = courseList.stream().anyMatch(singleCourse -> singleCourse.getCourseCode().equalsIgnoreCase(courseCode));

            if (alreadyPresent) {
                throw new AlreadyExistsException("This course Already Exist, Course Code: " + courseCode);
            } else {
                courseList.add(dbCourse);
            }
        }

        departmentRepository.save(dbDepartment);
        return dbDepartment;
    }

    public Department updateDepartmentInfo(String departmentCode, Department department) {
        Optional<Department> departmentOptional = departmentRepository.findByDepartmentCode(departmentCode);
        if (departmentOptional.isEmpty()) {
            throw new NotFoundException("Invalid Department Id");
        }
        Department dbDepartment = departmentOptional.get();
        dbDepartment.setDepartmentName(dbDepartment.getDepartmentName());
        dbDepartment.setDepartmentShortName(dbDepartment.getDepartmentShortName());
        dbDepartment.setDepartmentCode(dbDepartment.getDepartmentCode());
        dbDepartment.setDepartmentGroupName(department.getDepartmentGroupName());
        dbDepartment.setDepartmentGroupCode(department.getDepartmentGroupCode());
        return departmentRepository.save(department);
    }

    public Department findByDepartmentId(String departmentId) {
        return departmentRepository.findById(departmentId).orElseThrow(() -> new NotFoundException("Invalid department departmentCode: " + departmentId));
    }

    public Department findByDepartmentCode(String departmentCode) {
        return departmentRepository.findByDepartmentCode(departmentCode).orElseThrow(() -> new NotFoundException("Invalid department departmentCode: " + departmentCode));
    }

    public List<Department> findAllDepartment() {
        return departmentRepository.findAll();
    }

    private boolean valid(Department department) {
        return department != null && department.getDepartmentName() != null && !department.getDepartmentName().isEmpty() && department.getDepartmentShortName() != null && !department.getDepartmentShortName().isEmpty() && department.getDepartmentCode() != null && !department.getDepartmentCode().isEmpty() && !codeExists(department.getDepartmentCode());
    }

    private boolean codeExists(String code) {
        return departmentRepository.findByDepartmentCode(code).isPresent();
    }


    @Transactional
    public Department removeCourseByCourseId(String departmentCode, String courseCode) {
        Optional<Department> departmentOptional = departmentRepository.findByDepartmentCode(departmentCode);

        if (departmentOptional.isEmpty()) {
            throw new NotFoundException("Invalid Department Id");
        }

        Department dbDepartment = departmentOptional.get();

        // Validate courseCode if needed
        if (courseCode == null || courseCode.isEmpty()) {
            throw new IllegalArgumentException("Invalid courseCode");
        }

        // Remove the course directly from the department
        dbDepartment.getCourseList().removeIf(singleCourse -> singleCourse.getCourseCode().equalsIgnoreCase(courseCode));

        // Save the modified department
        return departmentRepository.save(dbDepartment);
    }

}
