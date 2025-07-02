package org.scaleorange.spring_s3_app.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {

	public String saveFile(MultipartFile file);
	
	public byte[] dowloadFile(String name);
	
	public String deletFile(String name);
	
	public List<String> allFilesList();
	
}
