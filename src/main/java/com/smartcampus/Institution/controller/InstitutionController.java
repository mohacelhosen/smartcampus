package com.smartcampus.Institution.controller;

import com.smartcampus.common.ApiResponse;
import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.common.RequestId;
import com.smartcampus.exception.IncompleteDataException;
import com.smartcampus.Institution.model.Institution;
import com.smartcampus.Institution.service.InstitutionService;
import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/university/institution")
@CrossOrigin("*")
public class InstitutionController {
    private static final Logger logger = LoggerFactory.getLogger(InstitutionController.class);
    @Autowired
    private InstitutionService institutionService;
    @PostMapping("/register")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Institution>> registerInstitution(@RequestBody Institution institution){
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        ApiResponse<Institution> response = new ApiResponse<>();
        response.setTimestamp(time);
        response.setEndpoint("/api/v1/university/institution/register");
        String requestId = RequestId.generateRequestId();
        try{
            Institution register = institutionService.register(institution);
            response.setData(register);
            response.setStatus(HttpStatus.CREATED.value());
            response.setMessage("Institution Register successfully");
            logger.info("InstitutionController::registerInstitution, Institution Register successfully. Time:{}, Request Id:{}, Institution Name:{}",time,requestId, institution.getInstitutionName());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (IncompleteDataException e){
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setData(null);
            logger.error("InstitutionController::registerInstitution, Institution  Fail to Register. Time:{}, Request Id:{}, Input:{}, Message:{}",time,requestId, institution, e.getMessage(), e);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/find-institution-by-code")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Institution>> findInstitutionByCode(@RequestParam String institutionCode){
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        ApiResponse<Institution> response = new ApiResponse<>();
        response.setTimestamp(time);
        response.setEndpoint("/api/v1/university/institution/find-institution-by-code");
        String requestId = RequestId.generateRequestId();
        try{
            Institution register = institutionService.findByInstitutionByCode(institutionCode);
            response.setData(register);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Institution Retrieve successfully");
            logger.info("InstitutionController::findInstitutionByCode, Institution Retrieve successfully. Time:{}, Request Id:{}, Institution Name:{}",time,requestId, institutionCode);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (IncompleteDataException e){
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setData(null);
            logger.error("InstitutionController::findInstitutionByCode, Fail to Retrieve Institution Information. Time:{}, Request Id:{}, Input:{}, Message:{}",time,requestId, institutionCode, e.getMessage(), e);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/find-all")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<Institution>>> findAllInstitution(){
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        ApiResponse<List<Institution>> response = new ApiResponse<>();
        response.setTimestamp(time);
        response.setEndpoint("/api/v1/university/institution/find-all");
        String requestId = RequestId.generateRequestId();
        try{
            List<Institution> register = institutionService.getAllInstitution();
            response.setData(register);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Institution Retrieve successfully");
            logger.info("InstitutionController::findAllInstitution, Institution Retrieve successfully. Time:{}, Request Id:{}",time,requestId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setData(null);
            logger.error("InstitutionController::findAllInstitution, Fail to Retrieve Institution Information. Time:{}, Request Id:{},  Message:{}",time,requestId, e.getMessage(), e);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete-institution-by-code")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @Hidden
    public ResponseEntity<ApiResponse<String>> deleteInstitutionByCode(@RequestParam String institutionCode){
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        ApiResponse<String> response = new ApiResponse<>();
        response.setTimestamp(time);
        response.setEndpoint("/api/v1/university/delete-institution-by-code");
        String requestId = RequestId.generateRequestId();
        try{
            String institutionStatus = institutionService.deleteInstitutionByCode(institutionCode);
            response.setData(institutionStatus);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Institution Register successfully");
            logger.info("InstitutionController::deleteInstitutionByCode, Institution Delete successfully. Time:{}, Request Id:{}, Institution Name:{}",time,requestId, institutionCode);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (IncompleteDataException e){
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setData(null);
            logger.error("InstitutionController::deleteInstitutionByCode, Fail to Delete Institution. Time:{}, Request Id:{}, Input:{}, Message:{}",time,requestId, institutionCode, e.getMessage(), e);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
