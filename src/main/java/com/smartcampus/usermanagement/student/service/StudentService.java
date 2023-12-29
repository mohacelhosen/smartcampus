package com.smartcampus.usermanagement.student.service;

import com.smartcampus.Institution.model.Institution;
import com.smartcampus.Institution.service.InstitutionService;
import com.smartcampus.security.model.CustomUserDetails;
import com.smartcampus.security.repository.UserRepository;
import com.smartcampus.usermanagement.student.model.StudentEntity;
import com.smartcampus.usermanagement.student.repository.StudentRepository;
import com.smartcampus.admin.model.Admin;
import com.smartcampus.admin.repository.AdminRepository;
import com.smartcampus.common.GeneralConstants;
import com.smartcampus.common.HtmlContentReplace;
import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.email.dto.MailDto;
import com.smartcampus.email.service.EmailService;
import com.smartcampus.exception.IncompleteDataException;
import com.smartcampus.exception.NotFoundException;
import com.smartcampus.usermanagement.teacher.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.LongStream;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    UserRepository userRepository;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    @Autowired
    private InstitutionService institutionService;

    public StudentEntity registerStudent(StudentEntity student) {
        if (student == null) {
            throw new RuntimeException("Student entity can't be null");
        }
        if (student.getEmail() == null || student.getEmail().isEmpty()) {
            throw new RuntimeException("Email Can't be Null !");
        }

        Optional<StudentEntity> optionalStudent = studentRepository.findByEmail(student.getEmail());
        if (optionalStudent.isPresent()) {
            throw new RuntimeException("Student already Register with this email::" + student.getEmail());
        }
        Institution institution = institutionService.findByInstitutionByCode(student.getInstitutionCode());
        student.setInstitutionName(institution.getInstitutionName());

        String registrationId = String.valueOf(UUID.randomUUID().getMostSignificantBits()).replace("-", "");
        student.setRegistrationId(registrationId);
        student.setCreateDate(new ModelLocalDateTime(null));
        StudentEntity saveStudent = studentRepository.save(student);

        String content = HtmlContentReplace.replaceHtmlContent("student-registration", student.getFirstName(), student.getFirstName(), "Student", student.getEmail(), registrationId);
        sendMail("Admission Application Received: " + saveStudent.getFirstName() + " for " + saveStudent.getStudyPlan().getDepartmentName() + " at Smart Campus", student.getEmail(), content, "Your application is received");

        List<CustomUserDetails> facultyHiring = userRepository.findAllByAuthorities(Collections.singletonList("ROLE_ADMISSION_OFFICER"), student.getInstitutionCode());
        if (facultyHiring.size() == 1) {
            String htmlContent = HtmlContentReplace.replaceHtmlContent("verify", facultyHiring.get(0).getFullName(), saveStudent.getFirstName() + " " + student.getLastName(), "Student", saveStudent.getEmail(), registrationId);
            sendMail("Action Needed: Verify Student Application ASAP- " + saveStudent.getRegistrationId(), facultyHiring.get(0).getEmail(), htmlContent, "Please update the teacher application");
        } else {
            for (CustomUserDetails facultyMember : facultyHiring) {
                executorService.execute(() -> {
                    String htmlContent = HtmlContentReplace.replaceHtmlContent("verify", facultyMember.getFullName(), saveStudent.getFirstName() + " " + student.getLastName(), "Student", saveStudent.getEmail(), registrationId);
                    sendMail("Action Needed: Verify Student Application ASAP- " + saveStudent.getRegistrationId(), facultyMember.getEmail(), htmlContent, "Please update the teacher application");
                });
            }
        }
        return saveStudent;
    }

    public StudentEntity updateStudentInfo(StudentEntity student) {
        // Ensure that student academic ID is provided and not empty
        if (student.getStudentAcademicId() == null || student.getStudentAcademicId().trim().isEmpty()) {
            throw new IncompleteDataException("Student academic ID is required.");
        }

        // Retrieve the existing student entity from the database
        Optional<StudentEntity> studentEntityOptional = studentRepository.findByStudentAcademicId(student.getStudentAcademicId());
        StudentEntity dbStudent = studentEntityOptional.orElseThrow(() -> new NotFoundException("Student not found with academic ID: " + student.getStudentAcademicId()));

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

    private void sendMail(String subject, String userEmail, String htmlContent, String text) {

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
            sendMail("Update on Your Application at Smart Campus", student.getEmail(), content, "Update on Your Application at Smart Campus");
            return "Student with registration ID: " + registrationId + " deleted successfully.";
        } catch (Exception e) {
            throw new RuntimeException("Error deleting teacher with registration ID: " + registrationId, e);
        }
    }

    public String lastStudentIdSpecificDepartment(String semesterTerm2Digit, String departmentCode3Digit, Integer semesterNumber) {
        List<StudentEntity> totalStudentInDptAndSemester = studentRepository.findByDepartmentCodeAndSemesterNumber(departmentCode3Digit, semesterNumber);

        // Find the maximum student academic ID
        Integer maxStudentAcademicId = totalStudentInDptAndSemester.stream().map(student -> Integer.parseInt(student.getStudentAcademicId())).max(Integer::compareTo) // Using method reference for comparison
                .orElse(0); // Default value if the list is empty

        String last2DigitOfYear = String.valueOf(LocalDate.now().getYear()).substring(2);
        if (maxStudentAcademicId == 0) {
            return last2DigitOfYear + semesterTerm2Digit + departmentCode3Digit + GeneralConstants.STUDENT_INITIAL_ACADEMIC_ID + 1;
        } else {
            return String.valueOf(maxStudentAcademicId + 1);
        }
    }


    public Long nextStudentId(String searchEmail, String institutionCode, String departmentCode) {

        // Check if a student with the given email already exists
        List<StudentEntity> studentList = studentRepository.findByInstitutionCodeAndDepartmentCodeAndSemesterNumber(institutionCode, departmentCode, 1);

        if (!studentList.isEmpty()) {
            for (StudentEntity singleStudent : studentList) {
                if (singleStudent.getEmail() != null && singleStudent.getEmail().equalsIgnoreCase(searchEmail) && singleStudent.getStudentAcademicId() != null && !singleStudent.getStudentAcademicId().isEmpty()) {
                    throw new RuntimeException("Student already has an academic ID");
                }
            }
        }

        // Calculate the maximum student ID
        long maxStudentId = studentList.stream()
                .filter(student -> student.getStudentAcademicId() != null)
                .mapToLong(student -> {
                    try {
                        return Long.parseLong(student.getStudentAcademicId());
                    } catch (NumberFormatException e) {
                        return 0;  // Return a default value or skip the value
                    }
                })
                .max()
                .orElse(0L);  // Return a default value if the stream is empty

        return maxStudentId;  // Return the next available student ID
    }

    public StudentEntity findByRegistrationId(String studentRegistrationId) {
        int length = studentRegistrationId.length();
        Optional<StudentEntity> studentEntity;
        if(length>15){
            studentEntity = studentRepository.findByRegistrationId(studentRegistrationId);
        }else{
            studentEntity = studentRepository.findByStudentAcademicId(studentRegistrationId);
        }

        if(!studentEntity.isPresent()){
            throw new NotFoundException("Invalid  id::"+studentRegistrationId);
        }

        return studentEntity.get();
    }
}
