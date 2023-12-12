package com.smartcampus.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartcampus.library.model.LibraryCardInformation;
import com.smartcampus.library.service.LibraryService;

@RestController
@RequestMapping("/api/v1/university/library-card")
@CrossOrigin("*")
public class LibraryCardInformationController {
	@Autowired
	private LibraryService service;

	@PostMapping("/save")
	public ResponseEntity<LibraryCardInformation> saveLibraryCard(
			@RequestBody LibraryCardInformation libraryCardInformation) {
		LibraryCardInformation records = service.save(libraryCardInformation);
		return new ResponseEntity<>(records, HttpStatus.OK);
	}

	@PostMapping("/update")
	public ResponseEntity<LibraryCardInformation> updateLibraryCard(
			@RequestBody LibraryCardInformation libraryCardInformation) {
		LibraryCardInformation records = service.updateLibraryCardInfo(libraryCardInformation);
		return new ResponseEntity<>(records, HttpStatus.OK);
	}

	@PostMapping("/search")
	public ResponseEntity<LibraryCardInformation> findLibraryCardByAcademicId(@RequestParam String academicId) {
		LibraryCardInformation records = service.findLibraryCardByAcademicId(academicId);
		return new ResponseEntity<>(records, HttpStatus.OK);
	}

	@PostMapping("/search/{id}")
	public ResponseEntity<LibraryCardInformation> findRecordById(@PathVariable String id) {
		LibraryCardInformation records = service.findById(id);
		return new ResponseEntity<>(records, HttpStatus.OK);
	}

	@PostMapping("/delete")
	public ResponseEntity<String> deleteLibraryCard(@RequestParam String id) {
		String recordDelete = service.libraryCardDelete(id);
		return new ResponseEntity<>(recordDelete, HttpStatus.OK);
	}
}
