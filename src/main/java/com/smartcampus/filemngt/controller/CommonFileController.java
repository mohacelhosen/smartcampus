package com.smartcampus.filemngt.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.smartcampus.filemngt.model.CommonFile;
import com.smartcampus.filemngt.service.CommonFileService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/university/filemngt/")
@CrossOrigin("*")
public class CommonFileController {
	@Autowired
	private CommonFileService commonFileService;

	@PostMapping("/file/add")
	public ResponseEntity<String> addFile(@RequestParam("title") String title, @RequestParam("file") MultipartFile file)
			throws IOException {
		String id = commonFileService.saveFileOnMongo(title, file);
		return new ResponseEntity<>("File added successfully. Id: " + id, HttpStatus.CREATED);
	}

	@GetMapping("/file/download/{id}")
	public ResponseEntity<byte[]> downloadFile(@PathVariable String id) throws IOException {
		CommonFile commonFile = commonFileService.getCommonFile(id);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + commonFile.getTitle() + "\"")
				.body(commonFile.getStream().readAllBytes());
	}

	@GetMapping("/file/stream/{id}")
	public void streamFile(@PathVariable String id, HttpServletResponse response) throws IOException {
		CommonFile commonFile = commonFileService.getCommonFile(id);

		// Set response headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDispositionFormData("attachment", commonFile.getTitle());
		headers.setContentType(MediaType.parseMediaType(commonFile.getContentType()));

		// If the file is audio or video, stream the content
		if (isAudioOrVideo(commonFile.getContentType())) {
			headers.set(HttpHeaders.CONTENT_TYPE, commonFile.getContentType());
			response.setStatus(HttpStatus.OK.value());
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, headers.getContentDisposition().toString());
			response.setHeader(HttpHeaders.CONTENT_TYPE, headers.getContentType().toString());

			// Stream the content
			copyStream(commonFile.getStream(), response.getOutputStream());
			response.flushBuffer();
		} else {
			// If it's not audio or video, initiate a download
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, headers.getContentDisposition().toString());
			response.setHeader(HttpHeaders.CONTENT_TYPE, headers.getContentType().toString());

			// Download the content
			copyStream(commonFile.getStream(), response.getOutputStream());
			response.flushBuffer();
		}
	}

	private void copyStream(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[4096];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
	}

	private boolean isAudioOrVideo(String contentType) {
		// Check if the content type corresponds to audio or video
		return contentType.startsWith("audio/") || contentType.startsWith("video/");
	}
}
