package com.smartcampus.Student.service;

import com.smartcampus.Student.model.StudentEntity;
import com.smartcampus.Student.repository.StudentRepository;
import com.smartcampus.admin.model.Admin;
import com.smartcampus.admin.repository.AdminRepository;
import com.smartcampus.common.GeneralConstants;
import com.smartcampus.common.HtmlContentReplace;
import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.email.dto.MailDto;
import com.smartcampus.email.service.EmailService;
import com.smartcampus.exception.IncompleteDataException;
import com.smartcampus.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private AdminRepository adminRepository;

    public StudentEntity registerStudent(StudentEntity student) {
        if (student == null){
            throw new RuntimeException("Student entity can't be null");
        }
        if (student.getEmail() == null || student.getEmail().isEmpty()){
            throw new RuntimeException("Email Can't be Null !");
        }
        String registrationId = String.valueOf(UUID.randomUUID().getMostSignificantBits()).replace("-", "");
        student.setRegistrationId(registrationId);
        student.setCreateDate(new ModelLocalDateTime(null));
        StudentEntity saveStudent = studentRepository.save(student);
        String content = HtmlContentReplace.replaceHtmlContent("student-registration", student.getFirstName(),student.getFirstName(),"Student", student.getEmail(),registrationId);
        sendMail("Smart Campus Student Registration",student.getEmail(),content,"Your application is received");

        List<Admin> adminList = adminRepository.findAll();

        adminList.forEach(singleAdmin->{
            String htmlContent = HtmlContentReplace.replaceHtmlContent("verify",singleAdmin.getFullName(),student.getFirstName(),"Student", student.getEmail(),registrationId);
            sendMail("ASSP verify Teacher "+student.getRegistrationId(),singleAdmin.getEmail(), htmlContent,"As soon as possible verify the student");
        });
        return saveStudent;
    }

    public StudentEntity updateStudentInfo(StudentEntity student) {
        // Ensure that student academic ID is provided and not empty
        if (student.getStudentAcademicId() == null || student.getStudentAcademicId().trim().isEmpty()) {
            throw new IncompleteDataException("Student academic ID is required.");
        }

        // Retrieve the existing student entity from the database
        Optional<StudentEntity> studentEntityOptional = studentRepository.findByStudentAcademicId(student.getStudentAcademicId());
        StudentEntity dbStudent = studentEntityOptional.orElseThrow(() ->
                new NotFoundException("Student not found with academic ID: " + student.getStudentAcademicId()));

        // Update fields using reflection
        Field[] fields = student.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // Allows accessing private fields
            try {
                Object value = field.get(student);
                if (value != null && !(value instanceof String && ((String) value).trim().isEmpty())) {
                    field.set(dbStudent, value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error updating the student info. Failed to access field: " + field.getName(), e);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Error updating the student info. Invalid argument for field: " + field.getName(), e);
            }
        }

        try {
            // Save the updated student entity back to the database
            return studentRepository.save(dbStudent);
        } catch (Exception e) {
            throw new RuntimeException("Error saving the updated student info to the database.", e);
        }
    }

    private void sendMail(String subject, String userEmail, String htmlContent, String text){

        MailDto dto = new MailDto();
        dto.setSubject(subject);
        dto.setTextBody(text);
        dto.setTo(Collections.singletonList(userEmail));
        dto.setHtmlString(htmlContent);
        emailService.sendEmailWithAttachment(dto);
    }

    public String deleteStudentAccountByRegistrationId(String registrationId, String reason) {
        Optional<StudentEntity> studentEntityOptional = studentRepository.findByRegistrationId(registrationId);

        if (studentEntityOptional.isEmpty()) {
            throw new NotFoundException("Teacher not Found, teacher registration ID: " + registrationId);
        }

        try {
            StudentEntity student = studentEntityOptional.get();
            studentRepository.delete(student);
            String content = HtmlContentReplace.replaceHtmlDeleteContent(student.getFirstName() + " " + student.getLastName(), reason);
            sendMail("Update on Your Application at Smart Campus", student.getEmail(),content,"Update on Your Application at Smart Campus");
            return "Student with registration ID: " + registrationId + " deleted successfully.";
        } catch (Exception e) {
            throw new RuntimeException("Error deleting teacher with registration ID: " + registrationId, e);
        }
    }

    public String lastStudentIdSpecificDepartment(String semesterTerm2Digit, String departmentCode3Digit, Integer semesterNumber) {
        List<StudentEntity> totalStudentInDptAndSemester = studentRepository.findByDepartmentCodeAndSemesterNumber(departmentCode3Digit, semesterNumber);

        // Find the maximum student academic ID
        Integer maxStudentAcademicId = totalStudentInDptAndSemester.stream()
                .map(student -> Integer.parseInt(student.getStudentAcademicId()))
                .max(Integer::compareTo) // Using method reference for comparison
                .orElse(0); // Default value if the list is empty

        String last2DigitOfYear = String.valueOf(LocalDate.now().getYear()).substring(2);
        if (maxStudentAcademicId == 0){
            return last2DigitOfYear + semesterTerm2Digit + departmentCode3Digit + GeneralConstants.STUDENT_INITIAL_ACADEMIC_ID + 1;
        } else {
            return String.valueOf(maxStudentAcademicId + 1);
        }
    }



}
