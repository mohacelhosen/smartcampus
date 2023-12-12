package com.smartcampus.library.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartcampus.library.model.LibraryCardInformation;
import com.smartcampus.library.repository.LibraryCardInformationRepo;

@Service
public class LibraryService {
	@Autowired
	private LibraryCardInformationRepo libraryRepo;

	public LibraryCardInformation save(LibraryCardInformation libraryCardInformation) {
		return libraryRepo.save(libraryCardInformation);
	}

	public LibraryCardInformation findLibraryCardByAcademicId(String academicId) {
		return libraryRepo.findByAcademicId(academicId).get();
	}

	public LibraryCardInformation findById(String id) {
		return libraryRepo.findById(id).get();
	}

	public LibraryCardInformation updateLibraryCardInfo(LibraryCardInformation libraryCardInformation) {
		if (libraryCardInformation.getAcademicId() != null) {
			Optional<LibraryCardInformation> dbRecord = libraryRepo
					.findByAcademicId(libraryCardInformation.getAcademicId());
			if (dbRecord.get() != null) {
				return libraryRepo.save(libraryCardInformation);
			} else {
				throw new RuntimeException("student academic id  doesn't exit");
			}

		} else {
			throw new RuntimeException("student academic id required");
		}
	}

	public String libraryCardDelete(String id) {
		LibraryCardInformation libraryCardInformation = libraryRepo.findById(id).get();
		if (libraryCardInformation != null) {
			libraryRepo.deleteById(id);
			return "Delete Successfully";
		} else {
			throw new RuntimeException(" id  doesn't exit");
		}
	}

}
