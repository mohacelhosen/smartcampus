package com.smartcampus.filemngt.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.smartcampus.filemngt.model.Video;
import com.smartcampus.filemngt.service.VideoService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/university/filemngt/")
@CrossOrigin("*")
public class VideoController {

	@Autowired
	private VideoService videoService;

	@PostMapping("/videos/add")
	public ResponseEntity<String> addVideo(@RequestParam("title") String title,
			@RequestParam("file") MultipartFile file) throws IOException {
		String id = videoService.addVideo(title, file);
		return new ResponseEntity<>("Video added successfully. Id: " + id, HttpStatus.CREATED);
	}

	@PostMapping("/videos/{id}")
	public ResponseEntity<Map<String, Object>> getVideo(@PathVariable String id) {
		try {
			Video video = videoService.getVideo(id);

			if (video != null) {
				Map<String, Object> response = new HashMap<>();
				response.put("title", video.getTitle());
				response.put("url", "/api/v1/university/filemngt/videos/stream/" + id);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/videos/download/{id}")
	public void downloadVideo(@PathVariable String id, HttpServletResponse response) {
		try {
			Video video = videoService.getVideo(id);

			// Set response headers for download
			response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
			response.setHeader("Content-Disposition", "attachment; filename=" + video.getTitle());

			// Copy video stream to response output stream
			FileCopyUtils.copy(video.getStream(), response.getOutputStream());
		} catch (Exception e) {
			// Handle exceptions related to streaming, if needed
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/videos/stream/{id}")
	public void streamVideo(@PathVariable String id, HttpServletResponse response) {
		try {
			Video video = videoService.getVideo(id);

			// Set response headers
			response.setContentType("video/mp4"); // Adjust the MIME type as needed
			response.setHeader("Content-Disposition", "inline; filename=" + video.getTitle());

			// Copy video stream to response output stream
			FileCopyUtils.copy(video.getStream(), response.getOutputStream());
		} catch (Exception e) {
			// Handle exceptions related to streaming, if needed
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/videos/delete/{id}")
	public ResponseEntity<String> deleteVideo(@PathVariable String id) {
		try {
			videoService.deleteVideo(id);
			return new ResponseEntity<>("Video deleted successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Failed to delete video", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/videos/all")
	public ResponseEntity<List<String>> getAllVideosId() throws IOException {
		List<String> allFiles = videoService.getAllVideoIds();
		return new ResponseEntity<>(allFiles, HttpStatus.OK);
	}
}