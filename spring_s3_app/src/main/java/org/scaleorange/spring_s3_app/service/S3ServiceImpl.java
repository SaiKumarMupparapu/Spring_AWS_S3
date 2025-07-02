package org.scaleorange.spring_s3_app.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

@Service
public class S3ServiceImpl implements S3Service {
	
	@Value("${bucket}")
	private String bucket;

	private AmazonS3 s3;

	public S3ServiceImpl(AmazonS3 s3) {
		this.s3 = s3;
	}

	@Override
	public String saveFile(MultipartFile file) {
		String originalFilename = file.getOriginalFilename();

		try {
			File convertFile = convertFile(file);
			PutObjectResult result = s3.putObject(bucket, originalFilename, convertFile);
			return result.getContentMd5();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public byte[] dowloadFile(String filename) {
		try {
			S3Object object = s3.getObject(bucket, filename);
			S3ObjectInputStream objectContent = object.getObjectContent();
			return objectContent.readAllBytes();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String deletFile(String filename) {
        s3.deleteObject(bucket, filename);
		return "File deleted";
	}

	@Override
	public List<String> allFilesList() {
             ListObjectsV2Result result = s3.listObjectsV2(bucket);
         return  result.getObjectSummaries()
                      .stream()
                      .map(a->a.getKey())
                      .collect(Collectors.toList());
	}

	public File convertFile(MultipartFile file) throws IOException {

		File convertedFile = new File(file.getOriginalFilename());

		FileOutputStream outputStream = new FileOutputStream(convertedFile);
		outputStream.write(file.getBytes());
		outputStream.close();

		return convertedFile;

	}

}
