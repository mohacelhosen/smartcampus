package com.smartcampus.usermanagement.teacher.service;

import com.smartcampus.admin.model.Admin;
import com.smartcampus.admin.model.TeacherApprove;
import com.smartcampus.admin.repository.AdminRepository;
import com.smartcampus.common.GeneralConstants;
import com.smartcampus.common.HtmlContentReplace;
import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.common.RequestId;
import com.smartcampus.email.dto.MailDto;
import com.smartcampus.email.service.EmailService;
import com.smartcampus.exception.AlreadyExistsException;
import com.smartcampus.exception.NotFoundException;
import com.smartcampus.security.model.CustomUserDetails;
import com.smartcampus.security.repository.UserRepository;
import com.smartcampus.usermanagement.teacher.model.Teacher;
import com.smartcampus.usermanagement.teacher.repository.TeacherRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class TeacherService {
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private AdminRepository adminRepository;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    @Autowired
    private UserRepository userRepository;

    public Teacher registerTeacher(Teacher teacher){
        Optional<Teacher> teacherOptional = teacherRepository.findByEmail(teacher.getEmail());
        if (teacherOptional.isPresent()){
            throw new AlreadyExistsException("User already registered");
        }
        String registrationId = RequestId.generateRequestId();
        teacher.setRegistrationId(registrationId);
        teacher.setCreatedDate(new ModelLocalDateTime(null));
        String userName = teacher.getFirstName()+" "+teacher.getLastName();
        String userEmail = teacher.getEmail();

        List<CustomUserDetails> facultyHiring = userRepository.findAllByAuthorities(Collections.singletonList("ROLE_FACULTY_HIRING_COMMITTEE"));

        String registrationContent = HtmlContentReplace.replaceHtmlContent("teacher-registration", teacher.getFirstName(), userName, "Teacher", userEmail, registrationId);
        sendMail("Your Teacher Registration Application Has Been Received: " + teacher.getRegistrationId(), teacher.getEmail(), registrationContent, "We have received your application please wait for admin approval");

        for (CustomUserDetails facultyMember : facultyHiring) {
            executorService.execute(() -> {
                String htmlContent = HtmlContentReplace.replaceHtmlContent("verify", facultyMember.getFullName(), userName, "Teacher", userEmail, registrationId);
                sendMail("Action Needed: Verify Teacher Application ASAP- " + teacher.getRegistrationId(), facultyMember.getEmail(), htmlContent, "Please update the teacher application");
            });
        }

        return teacherRepository.save(teacher);
    }


    public Teacher approveTeacher(String teacherAcademicId,TeacherApprove teacherApprove){
        Optional<Teacher> teacherOptional = teacherRepository.findByRegistrationId(teacherApprove.getTeacherRegistrationId());
        if (teacherOptional.isEmpty()){
            throw  new NotFoundException("Teacher not Found, Registration Id: "+teacherApprove.getTeacherRegistrationId());
        }
        Teacher teacher = teacherOptional.get();
        teacher.setTeacherAcademicId(teacherAcademicId);
        teacher.setRoles(teacherApprove.getRole());
        teacher.setAccountEnabled(teacherApprove.getAccountEnabled());
        if(teacherApprove.getApplicationStatus().equalsIgnoreCase("APPROVE") && teacherApprove.getAccountEnabled()){
            teacher.setConfirmationDate(new ModelLocalDateTime(null));
        }
        teacher.setDepartment(teacherApprove.getDepartment());
        teacher.setApplicationStatus(teacherApprove.getApplicationStatus());
        teacher.setAvailabilitySchedule(teacherApprove.getEmploymentType());
        teacher.setPreferredTeachingHours(teacherApprove.getPreferredTeachingHours());
        teacher.setMonthlySalary(teacherApprove.getMonthlySalary());
        teacher.setCompensationDetails(teacherApprove.getCompensationDetails());
        teacher.setTenureStatus(teacherApprove.getTenureStatus());
        teacher.setEmploymentType(teacherApprove.getEmploymentType());

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
        Optional<Teacher> teacherOptional = teacherRepository.findByTeacherId(teacher.getTeacherAcademicId());
        if (teacherOptional.isEmpty()){
            throw  new NotFoundException("Invalid teacher id, teacher id:"+teacher.getTeacherAcademicId());
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
    public String nextTeacherId(String searchEmail, String institutionCode) {
        // Check if a teacher with the given email already exists
        List<Teacher> existingTeachersWithEmail = teacherRepository.findAllByInstitutionCode(institutionCode);

        if (!existingTeachersWithEmail.isEmpty()) {
            for (Teacher singleTeacher:existingTeachersWithEmail ){
                if (singleTeacher.getEmail() != null &&
                        singleTeacher.getEmail().equalsIgnoreCase(searchEmail) &&
                        singleTeacher.getTeacherAcademicId() != null &&
                        !singleTeacher.getTeacherAcademicId().isEmpty()) {
                    throw new RuntimeException("Teacher already has an academic ID");
                }
            }
        }

        List<Teacher> teacherList = teacherRepository.findAll();

        int maxTeacherId = teacherList.stream()
                .filter(teacher -> teacher.getTeacherAcademicId() != null)
                .mapToInt(teacher -> {
                    try {
                        return Integer.parseInt(teacher.getTeacherAcademicId());
                    } catch (NumberFormatException e) {
                        return 0;  // Return a default value or skip the value
                    }
                })
                .max()
                .orElse(GeneralConstants.TEACHER_INITIAL_ACADEMIC_ID);

        return String.valueOf(maxTeacherId + 1);
    }





    private void sendMail(String subject, String userEmail, String htmlContent, String text){
        MailDto dto = new MailDto();
        dto.setSubject(subject);
        dto.setTextBody(text);
        dto.setTo(Collections.singletonList(userEmail));
        dto.setHtmlString(htmlContent);
        emailService.sendEmailWithAttachment(dto);
    }

    public String deleteTeacherAccountByRegistrationId(String registrationId, String reason) {
        Optional<Teacher> teacherOptional = teacherRepository.findByRegistrationId(registrationId);

        if (teacherOptional.isEmpty()) {
            throw new NotFoundException("Teacher not Found, teacher registration ID: " + registrationId);
        }

        try {
            Teacher teacher = teacherOptional.get();
            teacherRepository.delete(teacher);
            String content = HtmlContentReplace.replaceHtmlDeleteContent(teacher.getFirstName() + " " + teacher.getLastName(), reason);
            sendMail("Update on Your Application at Smart Campus", teacher.getEmail(),content,"Update on Your Application at Smart Campus");
            return "Teacher with registration ID: " + registrationId + " deleted successfully.";
        } catch (Exception e) {
            throw new RuntimeException("Error deleting teacher with registration ID: " + registrationId, e);
        }
    }


}
