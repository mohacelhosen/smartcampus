package com.smartcampus.filemngt.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.smartcampus.filemngt.model.Video;

@Service
public class VideoService {

	private final GridFsTemplate gridFsTemplate;
	private final GridFsOperations operations;

	@Autowired
	public VideoService(GridFsTemplate gridFsTemplate, GridFsOperations operations) {
		this.gridFsTemplate = gridFsTemplate;
		this.operations = operations;
	}

	public String addVideo(String title, MultipartFile file) {
		try {
			if (title == null || file == null) {
				throw new IllegalArgumentException("Title and file must not be null.");
			}

			DBObject metaData = new BasicDBObject();
			metaData.put("type", "video");
			metaData.put("title", file.getOriginalFilename());

			try (InputStream inputStream = file.getInputStream()) {
				ObjectId id = gridFsTemplate.store(inputStream, file.getName(), file.getContentType(), metaData);
				return id.toString();
			}
		} catch (IOException e) {
			// Handle or log the exception appropriately
			throw new RuntimeException("Error adding video", e);
		}
	}

	public Video getVideo(String id) throws IllegalStateException, IOException {
		GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(new ObjectId(id))));
		Video video = new Video();
		video.setTitle(file.getMetadata().get("title").toString());
		video.setStream(operations.getResource(file).getInputStream());
		return video;
	}

	public void deleteVideo(String id) {
		gridFsTemplate.delete(new Query(Criteria.where("_id").is(new ObjectId(id))));
	}

	public List<String> getAllVideoIds() throws IOException {
		List<String> videoIds = new ArrayList<>();

		gridFsTemplate.find(new Query()).forEach(file -> videoIds.add(file.getObjectId().toString()));

		return videoIds;
	}
}
