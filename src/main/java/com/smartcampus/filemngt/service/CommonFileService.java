package com.smartcampus.filemngt.service;

import java.io.IOException;
import java.io.InputStream;

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
import com.smartcampus.filemngt.model.CommonFile;

import jakarta.activation.FileTypeMap;
import jakarta.activation.MimetypesFileTypeMap;

@Service
public class CommonFileService {
	@Autowired
	private GridFsTemplate gridFsTemplate;
	@Autowired
	private GridFsOperations operations;

	public String saveFileOnMongo(String title, MultipartFile file) {
		if (file == null) {
			throw new IllegalArgumentException("Title and file must not be null.");
		}

		DBObject metaData = new BasicDBObject();
		metaData.put("type", getContentType(file.getOriginalFilename()));
		metaData.put("title", file.getOriginalFilename());

		try (InputStream inputStream = file.getInputStream()) {
			ObjectId id = gridFsTemplate.store(inputStream, file.getOriginalFilename(), file.getContentType(),
					metaData);
			return id.toString();
		} catch (IOException e) {
			throw new RuntimeException("Something went wrong::" + e.getCause());
		}
	}

	private String getContentType(String fileName) {
		// Use Java Activation Framework (JAF) to determine content type based on file
		// extension
		FileTypeMap fileTypeMap = new MimetypesFileTypeMap();
		return fileTypeMap.getContentType(fileName);
	}
	
	
	public CommonFile getCommonFile(String id) throws IllegalStateException, IOException {
		GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(new ObjectId(id))));
		CommonFile commonFile = new CommonFile();
		commonFile.setTitle(file.getMetadata().get("title").toString());
		commonFile.setContentType(file.getMetadata().get("type").toString());
		commonFile.setStream(operations.getResource(file).getInputStream());
		return commonFile;
	}
	
}
