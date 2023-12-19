package com.smartcampus.Student.service;

import com.smartcampus.Student.model.StudentEntity;
import com.smartcampus.Student.repository.StudentRepository;
import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.email.dto.MailDto;
import com.smartcampus.email.service.EmailService;
import com.smartcampus.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.UUID;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private EmailService emailService;

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
        String content = replaceHtmlContent("student-registration",student.getFirstName(),student.getEmail(),registrationId);
        sendMail("Smart Campus Student Registration",student.getEmail(),content);

        return saveStudent;
    }

    private String replaceHtmlContent(String template, String userName, String userEmail, String userRegistrationId){
        String filePath = "Templates/"+template+".html";
        Path path = Paths.get(filePath);
        try{
            String originalContent = Files.readString(path);
            originalContent = originalContent.replace("[|###userName###|]",userName);
            originalContent = originalContent.replace("[|#userEmail#|]",userEmail);
            originalContent = originalContent.replace("[|#userName#|]",userName);
            return originalContent.replace("[|#userRegistrationId#|]",userRegistrationId);

        } catch (IOException e) {
            throw new RuntimeException("Error occurred during file content reading");
        }
    }

    private void sendMail(String subject, String userEmail, String htmlContent){

        MailDto dto = new MailDto();
        dto.setSubject(subject);
        dto.setTextBody("Smart campus: Student Registration");
        dto.setTo(Collections.singletonList(userEmail));
        dto.setHtmlString(htmlContent);
        emailService.sendEmailWithAttachment(dto);
    }
}
