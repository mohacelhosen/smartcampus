package com.smartcampus.classroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartcampus.classroom.model.Assignment;
import com.smartcampus.classroom.model.Submission;
import com.smartcampus.classroom.service.AssignmentService;

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
}
