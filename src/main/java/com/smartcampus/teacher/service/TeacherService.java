package com.smartcampus.teacher.service;

import com.smartcampus.admin.model.Admin;
import com.smartcampus.admin.repository.AdminRepository;
import com.smartcampus.admin.service.AdminService;
import com.smartcampus.common.GeneralConstants;
import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.common.RequestId;
import com.smartcampus.email.dto.MailDto;
import com.smartcampus.email.service.EmailService;
import com.smartcampus.exception.AlreadyExistsException;
import com.smartcampus.exception.NotFoundException;
import com.smartcampus.teacher.model.Certification;
import com.smartcampus.teacher.model.Teacher;
import com.smartcampus.teacher.repository.TeacherRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private AdminRepository adminRepository;

    public Teacher registerTeacher(Teacher teacher){
        Optional<Teacher> teacherOptional = teacherRepository.findById(teacher.getEmail());
        if (teacherOptional.isPresent()){
            throw  new AlreadyExistsException("User already registered");
        }
        teacher.setRegistrationId(RequestId.generateRequestId());
        teacher.setCreatedDate(new ModelLocalDateTime(null));
        String userName = teacher.getFirstName()+" "+teacher.getLastName();
        String userEmail = teacher.getEmail();
        String userRegistrationId = teacher.getRegistrationId();

        List<Admin> adminList = adminRepository.findAll();

        String registrationContent = replaceHtmlContent("teacher-registration", userName, userEmail, userRegistrationId);
        sendMail("Teacher Registration "+teacher.getRegistrationId(),teacher.getEmail(), registrationContent);

        adminList.forEach(singleAdmin->{
            String htmlContent = replaceHtmlContent("teacher-verify", userName, userEmail, userRegistrationId);
            sendMail("ASSP verify Teacher "+teacher.getRegistrationId(),singleAdmin.getEmail(), htmlContent);
        });
        return teacherRepository.save(teacher);
    }

    public Teacher approveTeacher(String teacherRegistrationId, String teacherId, String roles, String sectionName, String sectionCode, String approveStatus,  Boolean accountEnabled, String approveByAdminId, String approveByAdminName){
        Optional<Teacher> teacherOptional = teacherRepository.findByRegistrationId(teacherRegistrationId);
        if (teacherOptional.isEmpty()){
            throw  new NotFoundException("Teacher not Found, Registration Id: "+teacherRegistrationId);
        }
        Teacher teacher = teacherOptional.get();
        teacher.setTeacherId(teacherId);
        teacher.setRoles(roles);
        teacher.setSectionName(sectionName);
        teacher.setSectionCode(sectionCode);
        teacher.setApproveStatus(approveStatus);
        teacher.setAccountEnabled(accountEnabled);
        teacher.setApproveByAdminId(approveByAdminId);
        teacher.setApproveByAdminName(approveByAdminName);
        if(approveStatus.equalsIgnoreCase("APPROVE")){
            teacher.setConfirmationDate(new ModelLocalDateTime(null));
        }
        return teacherRepository.save(teacher);
    }
    public Teacher updateTeacherBasicInfo(Teacher teacher){
        Optional<Teacher> teacherOptional = teacherRepository.findById(teacher.getEmail());
        if (teacherOptional.isEmpty()){
            throw  new NotFoundException("User Email not found");
        }
        Teacher dbTeacher = teacherOptional.get();
        String id = dbTeacher.getId();
        BeanUtils.copyProperties(teacher,dbTeacher);
        dbTeacher.setId(id);
        return teacherRepository.save(teacher);
    }
    public Teacher updateTeacher(Teacher teacher){
        Optional<Teacher> teacherOptional = teacherRepository.findByTeacherId(teacher.getTeacherId());
        if (teacherOptional.isEmpty()){
            throw  new NotFoundException("Invalid teacher id, teacher id:"+teacher.getTeacherId());
        }
        Teacher dbTeacher = teacherOptional.get();
        String id = dbTeacher.getId();
        dbTeacher.setId(id);
        teacher.setId(id);
        return teacherRepository.save(teacher);
    }
    public Teacher findTeacherByRegistration(String teacherRegistrationId){
        Optional<Teacher> teacherOptional = teacherRepository.findByRegistrationId(teacherRegistrationId);
        if (teacherOptional.isEmpty()){
            throw  new NotFoundException("Teacher not Found, Registration Id: "+teacherRegistrationId);
        }
        return teacherOptional.get();
    }

    public Teacher findTeacherByEmail(String teacherEmail){
        Optional<Teacher> teacherOptional = teacherRepository.findByEmail(teacherEmail);
        if (teacherOptional.isEmpty()){
            throw  new NotFoundException("Teacher not Found, Email: "+teacherEmail);
        }
        return teacherOptional.get();
    }

    public Teacher findTeacherById(String id){
        Optional<Teacher> teacherOptional = teacherRepository.findById(id);
        if (teacherOptional.isEmpty()){
            throw  new NotFoundException("Teacher not Found, BD ID: "+id);
        }
        return teacherOptional.get();
    }

    public Teacher findTeacherByTeacherId(String teacherId){
        Optional<Teacher> teacherOptional = teacherRepository.findByTeacherId(teacherId);
        if (teacherOptional.isEmpty()){
            throw  new NotFoundException("Teacher not Found, teacher ID: "+teacherId);
        }
        return teacherOptional.get();
    }

    public List<Teacher> findAllTeacher(){
        return teacherRepository.findAll();
    }
    @Transactional
    public String nextTeacherId() {
        List<Teacher> teacherList = teacherRepository.findAll();

        int maxTeacherId = teacherList.stream()
                .mapToInt(teacher -> Integer.parseInt(teacher.getTeacherId()))
                .max()
                .orElse(GeneralConstants.TEACHER_INITIAL_ACADEMIC_ID);

        return String.valueOf(maxTeacherId+1 );
    }


    private void sendMail(String subject, String userEmail, String htmlContent){
        MailDto dto = new MailDto();
        dto.setSubject(subject);
        dto.setTo(Collections.singletonList(userEmail));
        dto.setHtmlString(htmlContent);
        emailService.sendEmailWithAttachment(dto);
    }

    public String replaceHtmlContent(String template, String userName, String userEmail, String userRegistrationId){
        String filePath = "Templates/"+template+".html";
        Path path = Paths.get(filePath);
        try{
            String originalContent = Files.readString(path);
            originalContent = originalContent.replace("##userName##",userName);
            originalContent = originalContent.replace("##userEmail##",userEmail);
            originalContent = originalContent.replace("##userName##",userName);
            return originalContent.replace("##userRegistrationId##",userRegistrationId);

        } catch (IOException e) {
            throw new RuntimeException("Error occurred during file content reading");
        }
    }

}
