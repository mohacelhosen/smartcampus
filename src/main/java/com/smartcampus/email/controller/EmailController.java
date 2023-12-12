package com.smartcampus.email.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartcampus.email.dto.MailDto;
import com.smartcampus.email.service.EmailService;

import jakarta.mail.MessagingException;


// Update EmailController
@RestController
@RequestMapping("/api/v1/email")
@CrossOrigin("*")

public class EmailController {
    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public String sendEmailWithAttachment(
            @ModelAttribute MailDto mailDto
    ) {
        try {
            emailService.sendEmailWithAttachment(mailDto);
            return "Email sent successfully";
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
            return "Failed to send email";
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

