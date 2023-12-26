package com.smartcampus.classroom.controller;

import com.smartcampus.admin.model.Admin;
import com.smartcampus.common.ApiResponse;
import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.smartcampus.classroom.model.Assignment;
import com.smartcampus.classroom.model.Submission;
import com.smartcampus.classroom.service.AssignmentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/university/classroom/assignment")
@CrossOrigin("*")
public class AssignmentController {

	@Autowired
	private AssignmentService assignmentService;

	@PostMapping("assignment/save")
//	@PreAuthorize("hasAnyRole('ROLE_TEACHER','ROLE_STUDENT', 'ROLE_ADMIN','ROLE_DEVELOPER')")
	public ResponseEntity<String> saveAssignment(@RequestBody Assignment assignment) {
		try {
			String assignmentId = assignmentService.assignmentSaveToDB(assignment);
			return new ResponseEntity<>(assignmentId, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("assignment/update")
//	@PreAuthorize("hasAnyRole('ROLE_TEACHER','ROLE_STUDENT', 'ROLE_ADMIN','ROLE_DEVELOPER')")
	public ResponseEntity<?> updateAssignment(@RequestBody Assignment assignment) {
		try {
			Assignment updatedAssignment = assignmentService.updateAssignment(assignment);
			return new ResponseEntity<>(updatedAssignment, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("assignment/submit/{classId}")
//	@PreAuthorize("hasAnyRole('ROLE_TEACHER','ROLE_STUDENT', 'ROLE_ADMIN','ROLE_DEVELOPER')")
	public ResponseEntity<?> submitAssignment(@PathVariable String classId, @RequestBody Submission submission) {
		try {
			boolean isSubmitted = assignmentService.submitAssignment(classId, submission);
			return isSubmitted ? new ResponseEntity<>("Assignment submitted successfully", HttpStatus.OK)
					: new ResponseEntity<>("Failed to submit assignment", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("assignment/delete/{classId}/{assignmentId}")
//	@PreAuthorize("hasAnyRole('ROLE_TEACHER','ROLE_DEVELOPER')")
	public ResponseEntity<?> deleteAssignment(@PathVariable String classId, @PathVariable String assignmentId) {
		try {
			boolean isDeleted = assignmentService.deleteAssignment(classId, assignmentId);
			return isDeleted ? new ResponseEntity<>("Assignment deleted successfully", HttpStatus.OK)
					: new ResponseEntity<>("Failed to delete assignment", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/show-all-assignment-by-class-id")
	public ResponseEntity<ApiResponse<List<Assignment>>> showAllAssignmentByClassId(@RequestParam String classId) {
		String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
		ApiResponse<List<Assignment>> response = new ApiResponse<>();
		response.setTimestamp(time);
		response.setEndpoint("/api/v1/university/admin/show-all-assignment-by-class-id");
		try{
			List<Assignment> assignmentList = assignmentService.findAllAssignmentByClassId(classId);
			response.setMessage("Successfully retrieve all the admin info");
			response.setData(assignmentList);
			response.setStatus(HttpStatus.OK.value());
			return new ResponseEntity<>(response, HttpStatus.OK);
		}catch (NotFoundException e) {
			response.setData(null);
			response.setStatus(HttpStatus.NOT_FOUND.value());
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}
}
