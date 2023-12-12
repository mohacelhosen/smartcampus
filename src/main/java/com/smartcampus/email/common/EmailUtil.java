package com.smartcampus.email.common;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartcampus.email.dto.ByteArrayFile;
import com.smartcampus.email.dto.DataSet;
import com.smartcampus.email.dto.MailDto2;
import com.smartcampus.email.service.EmailService;


@Service
public class EmailUtil {
    @Autowired
    private EmailService emailService;

    public void sendMail(
            ArrayList<String> toEmailList,
            ArrayList<String> ccEmailList,
            ArrayList<String> bccEmailList,
            String subject,
            String textBody,
            String htmlString,
            ArrayList<DataSet> properties,
            ArrayList<ByteArrayFile> attachmentFiles) {

        String replacedHtmlString = replaceProperties(htmlString, properties);

        // Send mail
        MailDto2 dto = new MailDto2();
        dto.setTo(toEmailList);

        if (ccEmailList != null && !ccEmailList.isEmpty()) {
            dto.setCc(ccEmailList);
        } else {
            dto.setCc(null); // Set cc to null if not provided
        }

        if (bccEmailList != null && !bccEmailList.isEmpty()) {
            dto.setBcc(bccEmailList);
        } else {
            dto.setBcc(null); // Set bcc to null if not provided
        }

        dto.setSubject(subject);
        dto.setTextBody(textBody);
        dto.setHtmlString(replacedHtmlString);

        if (attachmentFiles != null && !attachmentFiles.isEmpty()) {
            dto.setAttachmentFiles(attachmentFiles);
        } else {
            dto.setAttachmentFiles(null); // Set attachmentFiles to null if not provided
        }

        try {
            emailService.sendEmailWithAttachment(dto);
        } catch (Exception e) {
        }
    }

    public String replaceProperties(String htmlString, ArrayList<DataSet> properties) {
        String htmlStringReplaced = htmlString;
        for (DataSet property : properties) {
            String replaceCode = property.getPropertyCode();
            replaceCode = "[|#" + replaceCode + "#|]";
            htmlStringReplaced = htmlStringReplaced.replace(replaceCode, property.getPropertyValue());
        }
        return htmlStringReplaced;
    }
}
