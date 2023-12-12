package com.smartcampus.classroom.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartcampus.classroom.model.Exam;
import com.smartcampus.classroom.model.SingleClass;
import com.smartcampus.classroom.model.Submission;
import com.smartcampus.classroom.repoository.ExamRepo;
import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.exception.NotFoundException;

@Service
public class ExamService {
	@Autowired
	private ExamRepo examRepo;

	@Autowired
	private SingleClassService singleClassService;

	public List<Exam> getAllExams() {
		return examRepo.findAll();
	}

	public Exam getExamById(String examId) {
		return examRepo.findById(examId).orElseThrow(() -> new NotFoundException("Exam id not found!"));
	}

	// save the exam into db
	public String examSaveToDB(Exam exam) {
		if (exam.getClassId() == null || exam.getClassId().isEmpty()) {
			throw new IllegalArgumentException("Class id can't be null or empty");
		}

		try {
			SingleClass dbClass = singleClassService.findSingleClass(exam.getClassId());
			exam.setExamCreationTime(new ModelLocalDateTime(null));
			if (dbClass != null) {
				String examId = examRepo.save(exam).getId();
				List<String> examsList = dbClass.getExamsList();
				examsList.add(examId);
				dbClass.setExamsList(examsList);
				return examId;
			} else {
				throw new NotFoundException("Class not found for id: " + exam.getClassId());
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to save exam", e);
		}
	}

	// update the exam info
	public Exam updateExam(Exam updatedExam) {
		if (updatedExam.getId() == null || updatedExam.getId().isEmpty()) {
			throw new RuntimeException("Exam id can't be null or empty");
		}

		return examRepo.findById(updatedExam.getId()).map(dbExam -> {
			dbExam.setClassTitle(updatedExam.getClassTitle());
			dbExam.setClassDescription(updatedExam.getClassDescription());
			dbExam.setExamDocumentIds(updatedExam.getExamDocumentIds());
			dbExam.setExamDate(updatedExam.getExamDate());
			dbExam.setGraded(updatedExam.isGraded());
			dbExam.setMarkForThisExam(updatedExam.getMarkForThisExam());

			// Set update time
			dbExam.setExamUpdateTime(new ModelLocalDateTime(null));

			return examRepo.save(dbExam);
		}).orElseThrow(() -> new NotFoundException("Exam not found for ID: " + updatedExam.getId()));
	}

	// update the exam info
	public Exam updateExam(String examId, Exam updatedExam) {
		Optional<Exam> existingExam = examRepo.findById(examId);
		if (existingExam.isPresent()) {
			Exam exam = existingExam.get();
			exam.setExamUpdateTime(new ModelLocalDateTime(null));
			exam.setClassDescription(updatedExam.getClassDescription());
			exam.setClassTitle(updatedExam.getClassTitle());
			exam.setExamDate(updatedExam.getExamDate());
			exam.setExamDocumentIds(updatedExam.getExamDocumentIds());
			exam.setGraded(updatedExam.isGraded());
			exam.setMarkForThisExam(updatedExam.getMarkForThisExam());

			// You may want to set the modification time here
			return examRepo.save(exam);
		} else {
			throw new NotFoundException("Exam id not found!::" + examId);
		}
	}

	// submit the exam papers
	public String submitPappers(String examId, Submission studentInfoAndDocuments) {
		Optional<Exam> existingExam = examRepo.findById(examId);
		if (existingExam.isPresent()) {
			Exam exam = existingExam.get();

			// Check if submission is enabled
			if (exam.getSubmissionEnable()) {
				List<Submission> examSubmissions = exam.getExamSubmissions();
				studentInfoAndDocuments.setSubmissionDate(new ModelLocalDateTime(null));
				examSubmissions.add(studentInfoAndDocuments);
				examRepo.save(exam);
				return "Document submitted successfully";
			} else {
				return "Document submission is not allowed , please contact with your teacher";
			}
		} else {
			throw new NotFoundException("Exam id not found!::" + examId);
		}
	}

	// submission enable or disable
	public String updateSubmissionEnable(String examId, boolean submissionEnable) {
		Optional<Exam> existingExam = examRepo.findById(examId);
		if (existingExam.isPresent()) {
			Exam exam = existingExam.get();
			exam.setSubmissionEnable(submissionEnable);
			exam.setExamUpdateTime(new ModelLocalDateTime(null));
			examRepo.save(exam);
			return "SubmissionEnable updated successfully";
		} else {
			throw new NotFoundException("Exam id not found!::" + examId);
		}
	}

	public List<Exam> findAllStudentIds(String examId) {
		return examRepo.findAllStudentIds(examId);
	}

}
