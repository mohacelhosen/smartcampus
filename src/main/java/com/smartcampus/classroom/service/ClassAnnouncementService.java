package com.smartcampus.classroom.service;

import com.smartcampus.usermanagement.student.service.StudentService;
import com.smartcampus.classroom.model.ClassAnnouncement;
import com.smartcampus.classroom.repoository.ClassAnnouncementRepository;
import com.smartcampus.course.model.Course;
import com.smartcampus.course.service.CourseService;
import com.smartcampus.department.service.DepartmentServie;
import com.smartcampus.usermanagement.teacher.model.Teacher;
import com.smartcampus.usermanagement.teacher.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ClassAnnouncementService {
    @Autowired
    private ClassAnnouncementRepository classAnnouncementRepository;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private DepartmentServie departmentServie;
    @Autowired
    private StudentService studentService;
    @Autowired
    private CourseService courseService;
    public ClassAnnouncement createAnnouncement(String teacherAcademicId, String departmentCode, Integer semesterNumber, String courseCode, String classStartTime){

        List<ClassAnnouncement> announcementList = classAnnouncementRepository.findAllByDepartmentCodeAndSemesterNumber(departmentCode, semesterNumber);
        Optional<ClassAnnouncement> maxAnnouncement = announcementList.stream().max(Comparator.comparing(ClassAnnouncement::getAnnouncementNumber));

        Integer lastAnnouncementNumber = maxAnnouncement.map(ClassAnnouncement::getAnnouncementNumber).orElse(0);


        Course course = courseService.findByCourseCode(courseCode);
        Teacher teacher = teacherService.findTeacherByTeacherId(teacherAcademicId);

        ClassAnnouncement announcement =new ClassAnnouncement();
        announcement.setClassTitle(course.getCourseTitle());
        announcement.setTeacherName(teacher.getFirstName()+" "+teacher.getLastName());
        announcement.setDepartmentCode(departmentCode);
        announcement.setClassStartSession(classStartTime);
        announcement.setSemesterNumber(semesterNumber);
        announcement.setAnnouncementNumber(lastAnnouncementNumber+1);
        return classAnnouncementRepository.save(announcement);
    }
    public List<ClassAnnouncement> findAllByDepartmentCodeAndSemesterNumber(String departmentCode, Integer semesterNumber){
        return classAnnouncementRepository.findAllByDepartmentCodeAndSemesterNumber(departmentCode, semesterNumber);
    }
}
