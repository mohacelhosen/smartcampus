package com.smartcampus.semester.service;

import com.smartcampus.classroom.model.SingleClass;
import com.smartcampus.classroom.repoository.SingleClassRepository;
import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.course.model.Course;
import com.smartcampus.course.repository.CourseRepository;
import com.smartcampus.exception.AlreadyExistsException;
import com.smartcampus.exception.NotFoundException;
import com.smartcampus.semester.model.CourseAndTeacherAndClass;
import com.smartcampus.semester.model.Semester;
import com.smartcampus.semester.repository.SemesterRepository;
import com.smartcampus.usermanagement.teacher.model.Teacher;
import com.smartcampus.usermanagement.teacher.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SemesterService {

    @Autowired
    private SemesterRepository semesterRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private SingleClassRepository classRepository;

    public Semester registerSemester(Semester semester){
        if (valid(semester)){
            semester.setCreatedDateTime(new ModelLocalDateTime(null));
            return semesterRepository.save(semester);
        }else{
            throw new AlreadyExistsException("Department and Semester already Registered");
        }
    }

    public Semester addCourseAndTeacherAndClass(String semesterCode, String courseCode, String teacherId, String classJoinCode, String institutionCode) {
        Optional<Semester> optionalSemester = semesterRepository.findBySemesterCode(semesterCode);
        if (optionalSemester.isEmpty()) {
            throw new NotFoundException("Semester id is not valid. ID: " + semesterCode);
        }

        Optional<Course> optionalCourse = courseRepository.findByCourseCode(courseCode, institutionCode);
        if (optionalCourse.isEmpty()) {
            throw new NotFoundException("Course id is not valid. ID: " + courseCode);
        }

        Optional<Teacher> optionalTeacherById = teacherRepository.findByTeacherId(teacherId);
        Optional<Teacher> optionalTeacherByRegId = teacherRepository.findByRegistrationId(teacherId);

        if (optionalTeacherById.isEmpty() && optionalTeacherByRegId.isEmpty()) {
            throw new NotFoundException("Teacher id or registration id is not valid. ID: " + teacherId);
        }

        Optional<SingleClass> optionalSingleClass = classRepository.findByClassJoinCode(classJoinCode);
        if (optionalSingleClass.isEmpty()) {
            throw new NotFoundException("Class JoinCode is not valid. Code: " + classJoinCode);
        }

        Semester semester = optionalSemester.get();
        Course course = optionalCourse.get();

        Teacher teacher = optionalTeacherById.orElse(optionalTeacherByRegId.orElseThrow(() -> new NotFoundException("Teacher id or registration id is not valid. ID: " + teacherId)));
        SingleClass singleClass = optionalSingleClass.get();

        List<CourseAndTeacherAndClass> courseAndTeacherAndClasses = semester.getCourseAndTeacherAndClasses();

        // Initialize the list if it's null
        if (courseAndTeacherAndClasses == null) {
            courseAndTeacherAndClasses = new ArrayList<>();
            semester.setCourseAndTeacherAndClasses(courseAndTeacherAndClasses);
        }

        CourseAndTeacherAndClass ctac = new CourseAndTeacherAndClass();
        ctac.setCourse(course);
        ctac.setClassJoinCode(classJoinCode);
        ctac.setTeacherEmail(teacher.getEmail());
        ctac.setTeacherName(teacher.getFirstName() + " " + teacher.getLastName());
        ctac.setTeacherContactNumber(teacher.getContactNumber());

        courseAndTeacherAndClasses.add(ctac);

        return semesterRepository.save(semester);
    }


    public Semester findBySemesterCode(String semesterCode){
        Optional<Semester> semesterOptional = semesterRepository.findBySemesterCode(semesterCode);
        if (semesterOptional.isEmpty()){
            throw new RuntimeException("Semester code is not valid");
        }
        return semesterOptional.get();
    }

    public  List<Semester> findAllSemester(){
        return semesterRepository.findAll();
    }

    public  List<Semester> findAllSemesterByDepartmentCode(String departmentCode){
        return semesterRepository.findByDepartmentCode(departmentCode);
    }

    public String deleteDepartment(String semesterCode){
        Optional<Semester> semesterOptional = semesterRepository.findBySemesterCode(semesterCode);
        if (semesterOptional.isEmpty()){
            throw new RuntimeException("Semester code is not valid");
        }
        Semester semester = semesterOptional.get();
        semesterRepository.delete(semester);
        return semester.getSemesterNumber()+" Delete successfully";
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
