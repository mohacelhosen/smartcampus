package com.smartcampus.department.service;

import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.course.model.Course;
import com.smartcampus.course.service.CourseService;
import com.smartcampus.department.model.Department;
import com.smartcampus.department.repository.DepartmentRepository;
import com.smartcampus.exception.AlreadyExistsException;
import com.smartcampus.exception.IncompleteDataException;
import com.smartcampus.exception.NotFoundException;
import com.smartcampus.semester.model.Semester;
import com.smartcampus.semester.service.SemesterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServie {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private CourseService courseService;

    @Autowired
    private SemesterService semesterService;
    private static final Logger logger = LoggerFactory.getLogger(DepartmentServie.class);

    @Transactional
    public Department registerDepartment(Department department, Integer totalSemester) {
        if (valid(department)) {
            department.setCreatedDateTime(new ModelLocalDateTime(null));
            department.setCourseList(null);

            String departmentGroupName = department.getDepartmentGroupName();
            String[] departmentGroupWords = departmentGroupName.split(" ");

            StringBuilder departmentGroupCode = new StringBuilder();
            Arrays.stream(departmentGroupWords)
                    .filter(word -> !isExcludedWord(word))
                    .forEach(eachWord -> departmentGroupCode.append(eachWord.charAt(0)));

            department.setDepartmentGroupCode(String.valueOf(departmentGroupCode));

            String departmentName = department.getDepartmentName();
            String[] departmentWords = departmentName.split(" ");

            StringBuilder departmentCode = new StringBuilder();
            Arrays.stream(departmentWords)
                    .filter(word -> !isExcludedWord(word))
                    .forEach(eachWord -> departmentCode.append(eachWord.charAt(0)));

            department.setDepartmentGroupCode(String.valueOf(departmentGroupCode));
            department.setDepartmentCode(String.valueOf(departmentCode));

            Department savedDepartment = departmentRepository.save(department);

            for(int i = 1; i<=totalSemester; i++){
                Semester semester = new Semester();
                semester.setDepartmentCode(savedDepartment.getDepartmentCode());
                semester.setDepartmentName(savedDepartment.getDepartmentName());
                semester.setSemesterNumber(i);
                semester.setSemesterCode(savedDepartment.getDepartmentGroupCode()+"-"+savedDepartment.getDepartmentCode()+"-"+i);
                semesterService.registerSemester(semester);
            }
        } else {
            throw new IncompleteDataException("Incomplete department information or duplicate courseCode not allowed to register");
        }
        return department;
    }

    private boolean isExcludedWord(String word) {
        return word.equalsIgnoreCase("AND") ||
                word.equalsIgnoreCase("OR") ||
                word.equalsIgnoreCase("NOT") ||
                word.equalsIgnoreCase("OF") ||
                word.equalsIgnoreCase("BUT")||
                word.equalsIgnoreCase("&");
    }


    @Transactional
    public Department addCourseInDepartment(String departmentCode, String courseCode, String institutionCode) {
        Optional<Department> optionalDepartment = departmentRepository.findByDepartmentCode(departmentCode);

        if (optionalDepartment.isEmpty()) {
            throw new NotFoundException("Invalid department code: " + departmentCode);
        }

        Course dbCourse = courseService.findByCourseCode(courseCode,institutionCode);
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
        return department != null && department.getDepartmentName() != null && !department.getDepartmentName().isEmpty() && department.getDepartmentShortName() != null && !department.getDepartmentShortName().isEmpty();
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
