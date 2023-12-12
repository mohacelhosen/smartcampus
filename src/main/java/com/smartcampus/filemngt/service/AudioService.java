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
import com.smartcampus.filemngt.model.Audio;

@Service
public class AudioService {
	@Autowired
	private GridFsTemplate gridFsTemplate;
	@Autowired
	private GridFsOperations operations;

	public String addAudio(String title, MultipartFile file) {
		try {
			if (file == null) {
				throw new IllegalArgumentException("Title and file must not be null.");
			}
			DBObject metaData = new BasicDBObject();
			metaData.put("type", "audio");
			metaData.put("title", file.getOriginalFilename());

			try (InputStream inputStream = file.getInputStream()) {
				ObjectId id = gridFsTemplate.store(inputStream, file.getOriginalFilename(), file.getContentType(),
						metaData);
				return id.toString();
			}

		} catch (IOException e) {
			throw new RuntimeException("Error adding audio", e);
		}
	}

	public Audio getAudio(String id) throws IllegalStateException, IOException {
		GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(new ObjectId(id))));
		Audio audio = new Audio();
		audio.setTitle(file.getMetadata().get("title").toString());
		audio.setStream(operations.getResource(file).getInputStream());
		return audio;
	}

	public void deleteAudio(String id) {
		gridFsTemplate.delete(new Query(Criteria.where("_id").is(new ObjectId(id))));
	}

	public List<String> getAllAudioIds() throws IOException {
		List<String> audioIds = new ArrayList<>();

		gridFsTemplate.find(new Query()).forEach(file -> audioIds.add(file.getObjectId().toString()));

		return audioIds;
	}

}
