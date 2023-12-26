package com.smartcampus.classroom.service;

import java.util.List;
import java.util.Optional;

import com.smartcampus.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartcampus.classroom.model.Assignment;
import com.smartcampus.classroom.model.SingleClass;
import com.smartcampus.classroom.model.Submission;
import com.smartcampus.classroom.repoository.AssignmentRepository;
import com.smartcampus.common.ModelLocalDateTime;

@Service
public class AssignmentService {
	@Autowired
	private AssignmentRepository assignmentRepository;
	@Autowired
	private SingleClassService singleClassService;

	// save the assignment into db
	public String assignmentSaveToDB(Assignment assignment) {
		if (assignment.getClassId() == null || assignment.getClassId().isEmpty()) {
			throw new IllegalArgumentException("Class id can't be null or empty");
		}

		try {
			SingleClass dbClass = singleClassService.findSingleClass(assignment.getClassId());
			assignment.setAssignmentCreationTime(new ModelLocalDateTime(null));
			if (dbClass != null) {
				String assignmentId = assignmentRepository.save(assignment).getId();
				List<String> dbAssignmentList = dbClass.getAssignmentList();
				dbAssignmentList.add(assignmentId);
				dbClass.setAssignmentList(dbAssignmentList);
				return assignmentId;
			} else {
				throw new RuntimeException("Class not found for id: " + assignment.getClassId());
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to save assignment", e);
		}
	}

	// update assignment info except submission
	public Assignment updateAssignment(Assignment assignment) {
		if (assignment.getId() == null || assignment.getId().isEmpty()) {
			throw new RuntimeException("Class id can't be null or empty");
		}

		return assignmentRepository.findById(assignment.getId()).map(dbAssignment -> {
			dbAssignment.setClassTitle(assignment.getClassTitle());
			dbAssignment.setClassDescription(assignment.getClassDescription());
			dbAssignment.setAssignmentDocumentIds(assignment.getAssignmentDocumentIds());
			dbAssignment.setGraded(assignment.isGraded());
			dbAssignment.setMarkForThisAssignment(assignment.getMarkForThisAssignment());
			dbAssignment.setDueDate(assignment.getDueDate());
			// dbAssignment.setSubmissions(assignment.getSubmissions()); // Uncomment if
			// needed
			return assignmentRepository.save(dbAssignment);
		}).orElseThrow(() -> new RuntimeException("Assignment not found for ID: " + assignment.getId()));
	}

	// submit the assignment papers
	public boolean submitAssignment(String classId, Submission submission) {
		if (classId == null || classId.equalsIgnoreCase("")) {
			throw new RuntimeException("Class id can't be null or empty");
		}
		Optional<Assignment> optionalAssignment = assignmentRepository.findById(classId);
		if (optionalAssignment.isPresent()) {
			Assignment dbAssignment = optionalAssignment.get();
			List<Submission> submissions = dbAssignment.getSubmissions();
			submission.setSubmissionDate(new ModelLocalDateTime(null));
			submissions.add(submission);
			dbAssignment.setSubmissions(submissions);
			assignmentRepository.save(dbAssignment);
			return true;
		} else {
			throw new RuntimeException("Message:Invalid Assignment id");
		}

	}

	// delete assignment by id
	public boolean deleteAssignment(String classId, String assignmentId) {
		if (classId == null || classId.equalsIgnoreCase("")) {
			throw new RuntimeException("Class id can't be null or empty");
		}

		try {
			SingleClass dbClass = singleClassService.findSingleClass(classId);
			List<String> dbAssignmentList = dbClass.getAssignmentList();
			dbAssignmentList.remove(assignmentId);
			dbClass.setAssignmentList(dbAssignmentList);
			singleClassService.registerAClass(dbClass);
			assignmentRepository.deleteById(assignmentId);
			return true;
		} catch (Exception e) {
			throw new RuntimeException("Message: Invalid class id or Assignment id");
		}

	}

	public List<Assignment> findAllAssignmentByClassId(String classId){
		SingleClass singleClass = singleClassService.findSingleClass(classId);
		if (singleClass !=null){
		return assignmentRepository.findAllByClassId(classId);}else {
			throw new NotFoundException("Class Not Found");
		}
	}

}
