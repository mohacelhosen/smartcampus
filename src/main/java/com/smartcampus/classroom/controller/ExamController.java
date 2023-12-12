package com.smartcampus.classroom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartcampus.classroom.model.Exam;
import com.smartcampus.classroom.model.Submission;
import com.smartcampus.classroom.service.ExamService;

@RestController
@RequestMapping("/api/v1/university/classroom/exam")
@CrossOrigin("*")
public class ExamController {

	@Autowired
	private ExamService examService;

	@GetMapping("/all")
//	@PreAuthorize("hasAnyRole('ROLE_TEACHER','ROLE_STUDENT', 'ROLE_ADMIN','ROLE_DEVELOPER')")
	public ResponseEntity<List<Exam>> getAllExams() {
		List<Exam> exams = examService.getAllExams();
		return new ResponseEntity<>(exams, HttpStatus.OK);
	}

	@GetMapping("/{examId}")
//	@PreAuthorize("hasAnyRole('ROLE_TEACHER','ROLE_STUDENT', 'ROLE_ADMIN','ROLE_DEVELOPER')")
	public ResponseEntity<Exam> getExamById(@PathVariable String examId) {
		Exam exam = examService.getExamById(examId);
		return new ResponseEntity<>(exam, HttpStatus.OK);
	}

	@PostMapping("/save")
//	@PreAuthorize("hasAnyRole('ROLE_TEACHER','ROLE_STUDENT', 'ROLE_ADMIN','ROLE_DEVELOPER')")
	public ResponseEntity<String> saveExam(@RequestBody Exam exam) {
		String examId = examService.examSaveToDB(exam);
		return new ResponseEntity<>("Exam saved with ID: " + examId, HttpStatus.CREATED);
	}

	@PutMapping("/update/{examId}")
//	@PreAuthorize("hasAnyRole('ROLE_TEACHER','ROLE_DEVELOPER')")
	public ResponseEntity<Exam> updateExam(@PathVariable String examId, @RequestBody Exam updatedExam) {
		Exam exam = examService.updateExam(examId, updatedExam);
		return new ResponseEntity<>(exam, HttpStatus.OK);
	}

	@PostMapping("/submit/{examId}")
//	@PreAuthorize("hasAnyRole('ROLE_TEACHER','ROLE_STUDENT', 'ROLE_ADMIN','ROLE_DEVELOPER')")
	public ResponseEntity<String> submitPapers(@PathVariable String examId, @RequestBody Submission submission) {
		String result = examService.submitPappers(examId, submission);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PutMapping("/update-submission-enable/{examId}")
//	@PreAuthorize("hasAnyRole('ROLE_TEACHER','ROLE_DEVELOPER')")
	public ResponseEntity<String> updateSubmissionEnable(@PathVariable String examId,
			@RequestParam boolean submissionEnable) {
		String result = examService.updateSubmissionEnable(examId, submissionEnable);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/students/{examId}")
//	@PreAuthorize("hasAnyRole('ROLE_TEACHER','ROLE_STUDENT', 'ROLE_ADMIN','ROLE_DEVELOPER')")
	public ResponseEntity<List<Exam>> findAllStudentIds(@PathVariable String examId) {
		List<Exam> exams = examService.findAllStudentIds(examId);
		return new ResponseEntity<>(exams, HttpStatus.OK);
	}
}
