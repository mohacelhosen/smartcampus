package com.smartcampus.semester.service;

import com.smartcampus.classroom.model.SingleClass;
import com.smartcampus.classroom.repoository.SingleClassRepository;
import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.course.model.Course;
import com.smartcampus.course.repository.CourseRepository;
import com.smartcampus.course.service.CourseService;
import com.smartcampus.exception.AlreadyExistsException;
import com.smartcampus.exception.NotFoundException;
import com.smartcampus.semester.model.CourseAndTeacherAndClass;
import com.smartcampus.semester.model.Semester;
import com.smartcampus.semester.repository.SemesterRepository;
import com.smartcampus.teacher.model.Teacher;
import com.smartcampus.teacher.repository.TeacherRepository;
import com.smartcampus.teacher.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            semester.setCreatedBy(null);
            return semesterRepository.save(semester);
        }else{
            throw new AlreadyExistsException("Department and Semester already Registered");
        }
    }

    public Semester addCourseAndTeacherAndClass(String semesterCode, String courseCode, String teacherId, String classJoinCode){
        Optional<Semester> optionalSemester = semesterRepository.findBySemesterCode(semesterCode);
        if (optionalSemester.isEmpty()){
            throw new NotFoundException("Semester id is not valid. ID: "+semesterCode);
        }

        Optional<Course> optionalCourse = courseRepository.findByCourseCode(courseCode);
        if (optionalCourse.isEmpty()){
            throw new NotFoundException("Course id is not valid. ID: "+courseCode);
        }
        Optional<Teacher> optionalTeacher = teacherRepository.findByTeacherId(teacherId);
        if (optionalTeacher.isEmpty()){
            throw new NotFoundException("Teacher id is not valid. ID: "+courseCode);
        }
        Optional<SingleClass> optionalSingleClass = classRepository.findByClassJoinCode(classJoinCode);
        if (optionalSingleClass.isEmpty()){
            throw new NotFoundException("Class JoinCode is not valid. Code: "+classJoinCode);
        }

        Semester semester = optionalSemester.get();
        Course course = optionalCourse.get();
        Teacher teacher = optionalTeacher.get();
        SingleClass singleClass = optionalSingleClass.get();

        List<CourseAndTeacherAndClass> courseAndTeacherAndClasses = semester.getCourseAndTeacherAndClasses();

        CourseAndTeacherAndClass ctac = new CourseAndTeacherAndClass();
        ctac.setCourse(course);
        ctac.setClassJoinCode(classJoinCode);
        ctac.setTeacherEmail(teacher.getEmail());
        ctac.setTeacherName(teacher.getFirstName()+" "+teacher.getLastName());
        ctac.setTeacherContactNumber(teacher.getContactNumber());

        courseAndTeacherAndClasses.add(ctac);
        semester.setCourseAndTeacherAndClasses(courseAndTeacherAndClasses);
        return semesterRepository.save(semester);
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
