package com.smartcampus.filemngt.service;

import java.io.IOException;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.filemngt.model.Photo;
import com.smartcampus.filemngt.repository.PhotoRepository;

@Service
public class PhotoService {

	@Autowired
	private PhotoRepository photoRepo;

	public String addPhoto(MultipartFile file) throws IOException {
		Photo photo = new Photo();
		photo.setFileName(file.getOriginalFilename());
		photo.setImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
		photo.setUploadTime(new ModelLocalDateTime(null).getLocalDateTimeStringAMPM());
		photo.setFileSize(String.valueOf(file.getSize()));
		photo = photoRepo.save(photo);
		return photo.getId();
	}

	public Photo getPhoto(String id) {
		return photoRepo.findById(id).get();
	}
}
