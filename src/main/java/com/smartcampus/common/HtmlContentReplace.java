package com.smartcampus.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HtmlContentReplace {
    public static String replaceHtmlContent(String template, String dear, String userName, String applicantName ,String userEmail, String userRegistrationId){
        String filePath = "Templates/"+template+".html";
        Path path = Paths.get(filePath);
        try{
            String originalContent = Files.readString(path);
            originalContent = originalContent.replace("[|###dear###|]",dear);
            originalContent = originalContent.replace("[|###userName###|]",userName);
            originalContent = originalContent.replace("[|#ApplicantName#|]",applicantName);
            originalContent = originalContent.replace("[|#userEmail#|]",userEmail);
            originalContent = originalContent.replace("[|#userName#|]",userName);
            return originalContent.replace("[|#userRegistrationId#|]",userRegistrationId);

        } catch (IOException e) {
            throw new RuntimeException("Error occurred during file content reading");
        }
    }
    public static String replaceHtmlDeleteContent( String applicantName, String reason ){
        String filePath = "Templates/reject.html";
        Path path = Paths.get(filePath);
        try{
            String originalContent = Files.readString(path);
            originalContent = originalContent.replace("[|###dear###|]",applicantName);
            originalContent = originalContent.replace("[|###userName###|]",applicantName);
            originalContent = originalContent.replace("[|#reason#|]",reason);
            return originalContent;
        } catch (IOException e) {
            throw new RuntimeException("Error occurred during file content reading");
        }
    }

    public static String replaceHtmlApproveContent( String academicId, String password, String teacherStudent ){
        String filePath = "Templates/userid-password.html";
        Path path = Paths.get(filePath);
        try{
            String originalContent = Files.readString(path);
            originalContent = originalContent.replace("[|#userId#|]",academicId);
            originalContent = originalContent.replace("[|#password#|]",password);
            originalContent = originalContent.replace("[|#teacherstudent#|]",teacherStudent);
            return originalContent;
        } catch (IOException e) {
            throw new RuntimeException("Error occurred during file content reading");
        }
    }
}
