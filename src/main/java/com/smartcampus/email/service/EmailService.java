package com.smartcampus.email.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.smartcampus.email.dto.MailDto;
import com.smartcampus.email.dto.MailDto2;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	public void sendEmailWithAttachment(MailDto mailDto) throws MessagingException, IOException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

		if (mailDto.getAttachmentFiles() != null) {
			// Handle multiple attachment files
			for (MultipartFile attachmentFile : mailDto.getAttachmentFiles()) {
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				FileCopyUtils.copy(attachmentFile.getInputStream(), outputStream);
				byte[] attachmentBytes = outputStream.toByteArray();

				mimeMessageHelper.addAttachment(Objects.requireNonNull(attachmentFile.getOriginalFilename()),
						new ByteArrayResource(attachmentBytes));
			}
		}

		// Set from email properties
		mimeMessageHelper.setFrom("mohacel.hosen@lynerp.com");

		// Set multiple recipients
		for (String recipient : mailDto.getTo()) {
			mimeMessageHelper.addTo(recipient);
		}

		// Set cc recipients
		if (mailDto.getCc() != null) {
			for (String ccRecipient : mailDto.getCc()) {
				mimeMessageHelper.addCc(ccRecipient);
			}
		}

		// Set bcc recipients
		if (mailDto.getBcc() != null) {
			for (String bccRecipient : mailDto.getBcc()) {
				mimeMessageHelper.addBcc(bccRecipient);
			}
		}

		mimeMessageHelper.setSubject(mailDto.getSubject());
		mimeMessageHelper.setText(mailDto.getTextBody(), mailDto.getHtmlString());

		javaMailSender.send(mimeMessage);
	}

	public void sendEmailWithAttachment(MailDto2 mailDto) throws MessagingException, IOException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

		// Handle multiple attachment files
		for (int i = 0; mailDto.getAttachmentFiles() != null && i < mailDto.getAttachmentFiles().size(); i++) {
			mimeMessageHelper.addAttachment(
					Objects.requireNonNull(mailDto.getAttachmentFiles().get(i).getFileNameWithExtension()),
					new ByteArrayResource(mailDto.getAttachmentFiles().get(i).getContent()));
		}

		// Set from email properties
		mimeMessageHelper.setFrom("client1@lynerp.com");

		// Set multiple recipients
		for (String recipient : mailDto.getTo()) {
			mimeMessageHelper.addTo(recipient);
		}

		// Set cc recipients
		if (mailDto.getCc() != null) {
			for (String ccRecipient : mailDto.getCc()) {
				mimeMessageHelper.addCc(ccRecipient);
			}
		}

		// Set bcc recipients
		if (mailDto.getBcc() != null) {
			for (String bccRecipient : mailDto.getBcc()) {
				mimeMessageHelper.addBcc(bccRecipient);
			}
		}

		mimeMessageHelper.setSubject(mailDto.getSubject());
		mimeMessageHelper.setText(mailDto.getTextBody(), mailDto.getHtmlString());

		javaMailSender.send(mimeMessage);
	}

}