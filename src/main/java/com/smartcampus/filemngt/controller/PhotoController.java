package com.smartcampus.filemngt.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.smartcampus.filemngt.model.Photo;
import com.smartcampus.filemngt.service.PhotoService;

@RestController
@RequestMapping("/api/v1/university/filemngt")
@CrossOrigin("*")
public class PhotoController {
	@Autowired
	private PhotoService photoService;

	@PostMapping("/photos/add")
	public String addPhoto(@RequestParam("image") MultipartFile image)
			throws IOException {
		String id = photoService.addPhoto(image);
		return id;
	}

	@PostMapping("/photos/{id}")
    public ResponseEntity<Map<String, Object>> getPhotoInfo(@PathVariable String id) {
        Photo photo = photoService.getPhoto(id);
        if (photo != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("FileName", photo.getFileName());
            response.put("Base64image", Base64.getEncoder().encodeToString(photo.getImage().getData()));
            response.put("UploadTime",photo.getUploadTime());
            response.put("FileSize",String.valueOf((Long.parseLong(photo.getFileSize())/1024))+"KB");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
	
	@GetMapping("/photos/view/{id}")
	public ResponseEntity<byte[]> viewPhoto(@PathVariable String id) {
	    Photo photo = photoService.getPhoto(id);

	    HttpHeaders headers = new HttpHeaders();
	    byte[] imageData = photo.getImage().getData();

	    // Determine the content type based on the file type
	    String contentType = getContentType(photo.getFileName());
	    headers.setContentType(MediaType.parseMediaType(contentType));

	    // Set content disposition
	    headers.setContentDisposition(ContentDisposition.builder("inline").filename(photo.getFileName()).build());

	    return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
	}

	private String getContentType(String fileName) {
	    // Determine the content type based on the file extension
	    if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")) {
	        return "image/jpeg";
	    } else if (fileName.toLowerCase().endsWith(".png")) {
	        return "image/png";
	    } else if (fileName.toLowerCase().endsWith(".pdf")) {
	        return "application/pdf";
	    } else {
	        // Default to binary data if the file type is unknown
	        return "application/octet-stream";
	    }
	}


}
