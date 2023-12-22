package com.smartcampus.security.service;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.smartcampus.common.GeneralConstants;
import com.smartcampus.common.HtmlContentReplace;
import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.common.RandomPasswordGenerator;
import com.smartcampus.email.dto.MailDto;
import com.smartcampus.email.service.EmailService;
import com.smartcampus.exception.AlreadyExistsException;
import com.smartcampus.exception.NotFoundException;
import com.smartcampus.exception.UserNotFoundException;
import com.smartcampus.security.model.CustomUserDetails;
import com.smartcampus.security.model.LoginModel;
import com.smartcampus.security.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    // <------------------------ Register ----------------->
    public CustomUserDetails register(CustomUserDetails user) {
        String email = user.getEmail();
        boolean validEmail = isValidEmail(email);
        if (email != null && validEmail) {
            Optional<CustomUserDetails> dbUser = repository.findUserByEmail(email);
            if (dbUser.isPresent()) {
                throw new AlreadyExistsException("Already Registered");
            }
            // Generate a random password
            String password = RandomPasswordGenerator.generateRandomPassword(15);
            String encodePWD = passwordEncoder.encode(password);

            // Set the generated password for the user
            user.setPassword(encodePWD);
            user.setPreviousPassword(Collections.singletonList(encodePWD));
            user.setAccountCreationDateTime(new ModelLocalDateTime(null));

            // Assign roles based on the user's role
            String userRole = user.getRole();
            if (userRole != null) {
                switch (userRole.toUpperCase()) {
                    case "ADMIN":
                        user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_ADMIN));
                        break;
                    case "TEACHER":
                        user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_TEACHER));
                        break;
                    case "STUDENT":
                        user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_STUDENT));
                        break;
                    case "STAFF":
                        user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_STAFF));
                        break;
                    case "BOARDMEMBER":
                        user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_BOARD_MEMBER));
                        break;
                    case "GUEST":
                        user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_GUEST));
                        break;
                    case "FACULTY_HIRING_COMMITTEE":
                        user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_FACULTY_HIRING_COMMITTEE));
                        break;
                    case "VOLUNTEER_COORDINATOR":
                        user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_VOLUNTEER_COORDINATOR));
                        break;
                    case "ATHLETICS_DIRECTOR":
                        user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_ATHLETICS_DIRECTOR));
                        break;
                    case "INTERNATIONAL_STUDENT_ADVISOR":
                        user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_INTERNATIONAL_STUDENT_ADVISOR));
                        break;
                    case "COMMUNICATIONS_OFFICER":
                        user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_COMMUNICATIONS_OFFICER));
                        break;
                    case "HEALTH_SERVICES_PROVIDER":
                        user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_HEALTH_SERVICES_PROVIDER));
                        break;
                    case "SECURITY_OFFICER":
                        user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_SECURITY_OFFICER));
                        break;
                    case "RESIDENTIAL_LIFE_COORDINATOR":
                        user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_RESIDENTIAL_LIFE_COORDINATOR));
                        break;
                    case "EVENT_COORDINATOR":
                        user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_EVENT_COORDINATOR));
                        break;
                    case "HR_MANAGER":
                        user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_HR_MANAGER));
                        break;
                    case "RESEARCHER":
                        user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_RESEARCHER));
                        break;
                    case "REGISTRAR":
                        user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_REGISTRAR));
                        break;
                    case "DEAN":
                        user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_DEAN));
                        break;
                    case "EXTERNAL_PARTNER":
                        user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_EXTERNAL_PARTNER));
                        break;
                    case "IT_SUPPORT":
                        user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_IT_SUPPORT));
                        break;
                    case "ADVISOR":
                        user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_ADVISOR));
                        break;
                    case "ALUMNI":
                        user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_ALUMNI));
                        break;
                    case "FINANCIAL_AID_OFFICER":
                        user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_FINANCIAL_AID_OFFICER));
                        break;
                    case "LIBRARIAN":
                        user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_LIBRARIAN));
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid role: " + userRole);
                }
            }

            CustomUserDetails savedUser = repository.save(user);
            // sendMail(user.getEmail(), user.getUsername(), user.getUserId(), password);

            savedUser.setPassword(password);
            // Return the saved user
            return savedUser;

        } else {
            throw new NotFoundException("Email id can't be null or duplicate or invalid email");
        }
    }

    public CustomUserDetails addRole(String email, String userRole) {
        Optional<CustomUserDetails> userOptional = repository.findUserByEmail(email);

        if (userOptional.isPresent()) {
            CustomUserDetails user = userOptional.get();

            // Convert the authorities to a mutable list
            List<String> authorities = new ArrayList<>(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

            // Check if the role already exists
            if (!authorities.contains(userRole)) {
                // Add the new role
                authorities.add(userRole);
                user.setAuthorities(authorities);
                return repository.save(user);
            } else {
                // Role already exists, no action needed
                return user;
            }
        } else {
            throw new UserNotFoundException("User not found for email: " + email);
        }
    }

    public CustomUserDetails findByEmail(String email) {
        return repository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found for email: " + email));
    }

    // <------------------------ Login ------------------------->
    public boolean login(LoginModel loginInfo) {
        if (isInvalidLoginInfo(loginInfo)) {
            throw new RuntimeException("Incomplete user info provided for login");
        }

        logger.info("UserService::login, ❓loginInfo found::{}", true);
        Optional<CustomUserDetails> userEntity;
        if (loginInfo.getAcademicId().contains("@")) {
            userEntity = repository.findUserByEmail(loginInfo.getAcademicId());
        } else {
            userEntity = getUserById(loginInfo.getAcademicId());
        }

        if (userEntity.isPresent()) {
            if (!userEntity.get().isEnabled()) {
                logger.info("UserService::login, ❓account is not enabled::{}", false);
                throw new RuntimeException("Account is not enabled");
            }

            logger.info("UserService::login, ❓loginInfo exists::{}", true);
            boolean passwordMatches = passwordEncoder.matches(loginInfo.getPassword(), userEntity.get().getPassword());

            if (passwordMatches) {
                logger.info("UserService::login, ❓logging pwd match::{}", true);
                return true; // Successful login
            } else {
                logger.info("UserService::login, ❓logging pwd match::{}", false);
                throw new RuntimeException("Logging Password Incorrect");
            }
        } else {
            logger.info("UserServiceImpl::login, ❓loginInfo exists::{}", false);
            throw new RuntimeException("User Not Found");
        }
    }

    private boolean isInvalidLoginInfo(LoginModel loginInfo) {
        return loginInfo == null || loginInfo.getAcademicId() == null || loginInfo.getAcademicId().isEmpty() || loginInfo.getPassword() == null || loginInfo.getPassword().isEmpty();
    }

    private Optional<CustomUserDetails> getUserById(String userId) {
        return repository.findByAcademicId(userId);
    }

    public String updatePassword(String userId, String previousPassword, String newPassword) {
        Optional<CustomUserDetails> userOptional = repository.findByAcademicId(userId);

        if (userOptional.isPresent()) {
            CustomUserDetails user = userOptional.get();
            boolean passwordMatches = passwordEncoder.matches(previousPassword, user.getPassword());

            if (passwordMatches) {
                // Update the password fields
                String encodePWD = passwordEncoder.encode(newPassword);
                user.setPassword(encodePWD);
                List<String> previousPwd = user.getPreviousPassword();
                previousPwd.add(encodePWD);
                user.setPreviousPassword(previousPwd);
                user.setEnabled(true);
                user.setLastPasswordResetDate(new ModelLocalDateTime(null));

                // Save the updated user details
                CustomUserDetails userDetails = repository.save(user);

                // Send notification email
                String content = HtmlContentReplace.replaceHtmlUpdatePWDContent(user.getAcademicId(), newPassword);
                sendMail("Password Update Confirmation", user.getEmail(), content, "Your application is approve. ID:" + user.getAcademicId() + ", password:" + newPassword);

                logger.info("UserService:updatePassword, Total Previous PWD::" + Arrays.toString(userDetails.getPreviousPassword().toArray()));

                return "Successfully updated, check your email:: "+userDetails.getEmail();
            } else {
                throw new RuntimeException("Incorrect previous password");
            }
        } else {
            throw new NotFoundException("Invalid User Academic id:: "+userId);
        }
    }

    public String forgetPassword(String userId) {
        Optional<CustomUserDetails> userOptional;
        if (userId.contains("@")) {
            userOptional = repository.findUserByEmail(userId);
        } else {
            userOptional = repository.findByAcademicId(userId);
        }

        if (userOptional.isPresent()) {
            CustomUserDetails user = userOptional.get();

            // Generate a sufficiently secure random password
            String generateRandomPassword = RandomPasswordGenerator.generateRandomPassword(25);

            String encodePWD = passwordEncoder.encode(generateRandomPassword);
            user.setPassword(encodePWD);
            // Update the user's password fields
            user.setPassword(passwordEncoder.encode(generateRandomPassword));
            List<String> previousPwd = user.getPreviousPassword();
            previousPwd.add(encodePWD);
            user.setPreviousPassword(previousPwd);
            user.setEnabled(true);
            user.setLastPasswordResetDate(new ModelLocalDateTime(null));

            // Save the updated user details
            repository.save(user);

            String content = HtmlContentReplace.replaceHtmlUpdatePWDContent(user.getAcademicId(), generateRandomPassword);
            sendMail("Password Update Confirmation", user.getEmail(), content, "Your application is approve. ID:" + user.getAcademicId() + ", password:" + generateRandomPassword);

            return "Update password send to your ::"+user.getEmail();
        } else {
            return "User not found";
        }
    }

    public String disableAccount(String userId) {
        Optional<CustomUserDetails> user = repository.findByAcademicId(userId);
        if (user.isPresent()) {
            user.get().setEnabled(false);
            repository.save(user.get());
            return "Account disabled successfully";
        }
        return "User not found";
    }

    public String enableAccount(String userId) {
        Optional<CustomUserDetails> user = repository.findByAcademicId(userId);
        if (user.isPresent()) {
            user.get().setEnabled(true);
            repository.save(user.get());
            return "Account enabled successfully"; // Corrected the return message
        }
        return "User not found";
    }

    public String accountLocked(String userId) {
        Optional<CustomUserDetails> user = repository.findByAcademicId(userId);
        if (user.isPresent()) {
            user.get().setAccountNonLocked(false); // Set accountNonLocked to false to lock the account
            repository.save(user.get());
            return "Account locked successfully";
        }
        return "User not found";
    }

    public String accountUnLocked(String userId) {
        Optional<CustomUserDetails> user = repository.findByAcademicId(userId);
        if (user.isPresent()) {
            user.get().setAccountNonLocked(true); // Set accountNonLocked to true to unlock the account
            repository.save(user.get());
            return "Account unlocked successfully";
        }
        return "User not found";
    }

    public String accountExpired(String userId) {
        Optional<CustomUserDetails> user = repository.findByAcademicId(userId);
        if (user.isPresent()) {
            user.get().setAccountNonExpired(false); // Set accountNonExpired to false to expire the account
            repository.save(user.get());
            return "Account expired";
        }
        return "User not found";
    }

    public String accountRenew(String userId) {
        Optional<CustomUserDetails> user = repository.findByAcademicId(userId);
        if (user.isPresent()) {
            user.get().setAccountNonExpired(true); // Set accountNonExpired to true to renew the account
            repository.save(user.get());
            return "Account renewed successfully";
        }
        return "User not found";
    }


    private void sendMail(String subject, String userEmail, String htmlContent, String text) {

        MailDto dto = new MailDto();
        dto.setSubject(subject);
        dto.setTextBody(text);
        dto.setTo(Collections.singletonList(userEmail));
        dto.setHtmlString(htmlContent);
        emailService.sendEmailWithAttachment(dto);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^(?=.{1,255}$)(?=.{1,64}@.{1,255}$)[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*){1,}[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
}
