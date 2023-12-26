package com.smartcampus.admin.service;

import com.smartcampus.Institution.model.Institution;
import com.smartcampus.Institution.service.InstitutionService;
import com.smartcampus.common.*;
import com.smartcampus.department.model.Department;
import com.smartcampus.department.repository.DepartmentRepository;
import com.smartcampus.usermanagement.student.model.StudentEntity;
import com.smartcampus.usermanagement.student.repository.StudentRepository;
import com.smartcampus.usermanagement.student.service.StudentService;
import com.smartcampus.admin.model.StudentApprove;
import com.smartcampus.admin.model.TeacherApprove;
import com.smartcampus.email.dto.MailDto;
import com.smartcampus.email.service.EmailService;
import com.smartcampus.exception.NotFoundException;
import com.smartcampus.security.repository.UserRepository;
import com.smartcampus.usermanagement.teacher.model.Teacher;
import com.smartcampus.usermanagement.teacher.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartcampus.admin.model.Admin;
import com.smartcampus.admin.repository.AdminRepository;
import com.smartcampus.security.model.CustomUserDetails;
import com.smartcampus.security.service.UserService;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private InstitutionService institutionService;
    @Autowired
    private DepartmentRepository departmentRepository;

    private ExecutorService executorService = Executors.newFixedThreadPool(10); // for a fixed thread pool with 10 threads


    @Transactional
    public Admin registerAdmin(Admin admin) {

        if (admin.getEmail() != null && !admin.getEmail().isEmpty()) {
            Institution institution = institutionService.findByInstitutionByCode(admin.getInstitutionCode());
            List<Admin> institutionAdminList = adminRepository.findAllByInstitutionCode(admin.getInstitutionCode());

            List<CustomUserDetails> superAdmin = userRepository.findAllByAuthorities(Collections.singletonList("ROLE_SUPER_ADMIN"), admin.getInstitutionCode());

            AtomicLong nextAdminId = new AtomicLong(10002L);
            // Set the next academicId
            if (!institutionAdminList.isEmpty()) {
                institutionAdminList.forEach(singleAdmin -> {
                    long currentId = Long.parseLong(singleAdmin.getAcademicId());
                    if (currentId > nextAdminId.get()) {
                        nextAdminId.set(currentId);
                    }
                });
                admin.setAcademicId(String.valueOf(nextAdminId.incrementAndGet()));
            } else {
                admin.setAcademicId(admin.getInstitutionCode() + "-" + nextAdminId.get());
            }
            admin.setRegistrationId(String.valueOf(UUID.randomUUID().getMostSignificantBits()).replace("-",""));

            Admin saveAdmin = adminRepository.save(admin);

            // Creating a CustomUserDetails instance
            CustomUserDetails cd = new CustomUserDetails();
            cd.setEmail(admin.getEmail());
            cd.setAcademicId(admin.getAcademicId());
            cd.setRole("ADMIN");
            cd.setFullName(admin.getFirstName() + " " + admin.getLastName());
            CustomUserDetails register = userService.register(cd);

            // Saving the admin
            String password = register.getPassword();
            String content = HtmlContentReplace.replaceHtmlApproveContent(admin.getAcademicId(), register.getPassword(), "ADMIN", register.getFullName());
            sendMail("Application approve", admin.getEmail(), content, "Your application is approve. ID:" + admin.getAcademicId() + ", password:" + register.getPassword());

            if (!institutionAdminList.isEmpty()) {
                institutionAdminList.forEach(singelAdmin -> {
                    String content2 = HtmlContentReplace.replaceHtmlContent("approve", singelAdmin.getNickName(), register.getFullName(), register.getFullName(), register.getEmail(), admin.getRegistrationId());
                    executorService.execute(() -> {
                        sendMail("Application approve", singelAdmin.getEmail(), content2, "Your application is approve. ID:" + admin.getAcademicId() + ", password:" + register.getPassword());
                    });
                });
            }

            if (!superAdmin.isEmpty()) {
                institutionAdminList.forEach(singelAdmin -> {
                    String content3 = HtmlContentReplace.replaceHtmlContent("approve", singelAdmin.getNickName(), register.getFullName(), register.getFullName(), register.getEmail(), admin.getRegistrationId());
                    executorService.execute(() -> {
                        sendMail("New Admin Added for "+admin.getInstitutionName(), singelAdmin.getEmail(), content3, "Your application is approve. ID:" + admin.getAcademicId() + ", password:" + register.getPassword());
                    });
                });
            }
            return saveAdmin;
        } else {
            throw new RuntimeException("Email cannot be null or empty");
        }
    }


    public Admin updateAdminInfo(Admin admin) {
        if (admin.getEmail() != null) {
            Optional<Admin> dbUserOptional = adminRepository.findByAcademicIdAndInstitutionCode(admin.getAcademicId(), admin.getInstitutionCode());
            if (dbUserOptional.isEmpty()) {
                throw new RuntimeException("Admin id not found");
            }

            Admin dbAdmin = dbUserOptional.get();
            Optional<CustomUserDetails> customUserOptional = userRepository.findByAcademicId(dbAdmin.getAcademicId());
            if (customUserOptional.isEmpty()) {
                throw new RuntimeException("Admin(SDB) id not found");
            }
            dbAdmin.setEmail(admin.getEmail());
            return adminRepository.save(dbAdmin);
        } else {
            throw new RuntimeException("Something went wrong");
        }
    }

    public Teacher teacherInformationCheckAndApprove(TeacherApprove teacherApprove) {


        Teacher dbTeacher = teacherService.findTeacherByRegistration(teacherApprove.getTeacherRegistrationId());
        if (dbTeacher == null) {
            throw new NotFoundException("Teacher not Found, Registration Id: " + teacherApprove.getTeacherRegistrationId());
        }

        String teacherAcademicId = teacherService.nextTeacherId(dbTeacher.getEmail(), dbTeacher.getInstitutionCode());
        String teacherRegistrationId = teacherApprove.getTeacherRegistrationId();
        String role = "Teacher";

        CustomUserDetails cd = new CustomUserDetails();
        cd.setEmail(dbTeacher.getEmail());
        cd.setAcademicId(teacherAcademicId);
        cd.setRole(role);
        cd.setFullName(dbTeacher.getFirstName() + " " + dbTeacher.getLastName());
        CustomUserDetails register = userService.register(cd);

        Teacher teacher = teacherService.approveTeacher(teacherAcademicId, teacherApprove);
        String content = HtmlContentReplace.replaceHtmlApproveContent(teacherAcademicId, register.getPassword(), "TEACHER", register.getFullName());
        sendMail("Official Approval: You're Now a Recognized Teacher with Us!", teacher.getEmail(), content, "Your application is approve. ID:" + teacherAcademicId + ", password:" + register.getPassword());

        if (teacher == null) {
            throw new NotFoundException("Teacher not Found, Registration Id: " + teacherRegistrationId);
        }

        return teacher;
    }

    public StudentEntity approveStudentAndGenerateIdPassword(StudentApprove studentApprove) {

        String registrationId = studentApprove.getRegistrationId();
        String applicationStatus = studentApprove.getApplicationStatus();
        String activateBy = studentApprove.getActivateBy();
        String semesterTerm2Digit = studentApprove.getSemesterTerm2Digit();
        String departmentCode3Digit = studentApprove.getDepartmentCode3Digit();
        Integer semesterNumber = studentApprove.getSemesterNumber();


        Optional<StudentEntity> optionalStudent = studentRepository.findByRegistrationId(registrationId);
        if (optionalStudent.isEmpty()) {
            throw new NotFoundException("Student not Found, Registration Id: " + registrationId);
        }
        String roles = "Student";
        String studentAcademicId = studentService.lastStudentIdSpecificDepartment(semesterTerm2Digit, departmentCode3Digit, semesterNumber);
        StudentEntity student = optionalStudent.get();
        if (applicationStatus.equalsIgnoreCase("APPROVE")) {
            student.setApplicationStatus(applicationStatus);
            CustomUserDetails cd = new CustomUserDetails();
            cd.setEmail(student.getEmail());
            cd.setAcademicId(studentAcademicId);
            cd.setRole(roles);
            cd.setFullName(student.getFirstName() + " " + student.getLastName());
            CustomUserDetails register = userService.register(cd);

            String content = HtmlContentReplace.replaceHtmlApproveContent(studentAcademicId, register.getPassword(), "STUDENT", register.getFullName());
            sendMail("Congratulations! Your Application is Now Approved.", student.getEmail(), content, "Your application is approve. ID:" + studentAcademicId + ", password:" + register.getPassword());
            return studentRepository.save(student);
        }
        {
            return studentRepository.save(student); // todo
        }
    }

    public List<Admin> allAdmin() {
        return adminRepository.findAll();
    }

    private void sendMail(String subject, String userEmail, String htmlContent, String text) {

        MailDto dto = new MailDto();
        dto.setSubject(subject);
        dto.setTextBody(text);
        dto.setTo(Collections.singletonList(userEmail));
        dto.setHtmlString(htmlContent);
        emailService.sendEmailWithAttachment(dto);
    }

    public List<Admin> showAllAdmin() {
        return adminRepository.findAll();
    }

    public StudentEntity approveStudent(String studentRegistrationNumber) {
        Optional<StudentEntity> optionalStudent = studentRepository.findByRegistrationId(studentRegistrationNumber);
        if (!optionalStudent.isPresent()){
            throw new NotFoundException("Student Registration invalid: "+studentRegistrationNumber);
        }
        StudentEntity student = optionalStudent.get();
        Optional<Department> departmentOptional = departmentRepository.findByDepartmentCodeAndInstitutionCode(student.getStudyPlan().getDepartmentCode(), student.getInstitutionCode());
        if (!departmentOptional.isPresent()){
            throw new NotFoundException("Department Not Found , Department Code:: "+student.getStudyPlan().getDepartmentCode()+"Institution Code:: "+student.getInstitutionCode());
        }
        Department department = departmentOptional.get();


        Long lastStudentAcademicId = studentService.nextStudentId(student.getEmail(), student.getInstitutionCode(), department.getDepartmentCode());

        String studentAcademicId = "";
        if (lastStudentAcademicId==0){
            studentAcademicId = IdGenerator.academicIdGenerator(student.getStudyPlan().getSemester().toString(), String.valueOf(department.getDepartmentCodeInNumber()), GeneralConstants.STUDENT_INITIAL_ACADEMIC_ID.toString());
        }else {
            studentAcademicId = String.valueOf(lastStudentAcademicId+1);
        }



        student.setStudentAcademicId(studentAcademicId);
        StudentEntity saveStudent = studentRepository.save(student);

        CustomUserDetails cd = new CustomUserDetails();
        cd.setEmail(saveStudent.getEmail());
        cd.setAcademicId(studentAcademicId);
        cd.setRole("STUDENT");
        cd.setFullName(saveStudent.getFirstName() + " " + saveStudent.getLastName());
        CustomUserDetails register = userService.register(cd);

        String content = HtmlContentReplace.replaceHtmlApproveContent(studentAcademicId, register.getPassword(), "TEACHER", register.getFullName());
        sendMail("Official Approval: You're Now a Recognized Student with Us!", saveStudent.getEmail(), content, "Your application is approve. ID:" + studentAcademicId + ", password:" + register.getPassword());

        if (saveStudent == null) {
            throw new NotFoundException("Student not Found, Registration Id: " + student.getRegistrationId());
        }

        return saveStudent;
    }

    public List<Admin> findAllByInstitutionCode(String institutionCode){
        return adminRepository.findAllByInstitutionCode(institutionCode);
    }

    public Admin findByAcademicIdAndInstCode(String academicId, String institutionCode){
        Optional<Admin> adminOptional = adminRepository.findByAcademicIdAndInstitutionCode(academicId, institutionCode);
        if (!adminOptional.isPresent()){
            throw new NotFoundException("Admin not found");
        }
        return adminOptional.get();
    }
    public Admin findByRegistrationId(String registrationId){
        Optional<Admin> adminOptional = adminRepository.findByRegistrationId(registrationId);
        if (!adminOptional.isPresent()){
            throw new NotFoundException("Admin not found");
        }
        return adminOptional.get();
    }
}
