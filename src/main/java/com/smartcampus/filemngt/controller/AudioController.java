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

import com.smartcampus.filemngt.model.Audio;
import com.smartcampus.filemngt.service.AudioService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/university/filemngt/")
@CrossOrigin("*")
public class AudioController {

    @Autowired
    private AudioService audioService;

    @PostMapping("/audios/add")
    public ResponseEntity<String> addAudio(@RequestParam("title") String title,
            @RequestParam("file") MultipartFile file) throws IOException {
        String id = audioService.addAudio(title, file);
        return new ResponseEntity<>("Audio added successfully. Id: " + id, HttpStatus.CREATED);
    }

    @PostMapping("/audios/{id}")
    public ResponseEntity<Map<String, Object>> getAudio(@PathVariable String id) {
        try {
            Audio audio = audioService.getAudio(id);

            if (audio != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("title", audio.getTitle());
                response.put("url", "/api/v1/university/filemngt/audios/stream/" + id);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/audios/download/{id}")
    public void downloadAudio(@PathVariable String id, HttpServletResponse response) {
        try {
            Audio audio = audioService.getAudio(id);

            // Set response headers for download
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-Disposition", "attachment; filename=" + audio.getTitle());

            // Copy audio stream to response output stream
            FileCopyUtils.copy(audio.getStream(), response.getOutputStream());
        } catch (Exception e) {
            // Handle exceptions related to streaming, if needed
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/audios/stream/{id}")
    public void streamAudio(@PathVariable String id, HttpServletResponse response) {
        try {
            Audio audio = audioService.getAudio(id);

            // Set response headers
            response.setContentType("audio/mpeg"); // Adjust the MIME type as needed
            response.setHeader("Content-Disposition", "inline; filename=" + audio.getTitle());

            // Copy audio stream to response output stream
            FileCopyUtils.copy(audio.getStream(), response.getOutputStream());
        } catch (Exception e) {
            // Handle exceptions related to streaming, if needed
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/audios/delete/{id}")
    public ResponseEntity<String> deleteAudio(@PathVariable String id) {
        try {
            audioService.deleteAudio(id);
            return new ResponseEntity<>("Audio deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete audio", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/audios/all")
    public ResponseEntity<List<String>> getAllAudiosId() throws IOException {
        List<String> allFiles = audioService.getAllAudioIds();
        return new ResponseEntity<>(allFiles, HttpStatus.OK);
    }
}

