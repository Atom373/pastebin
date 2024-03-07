package com.example.pastebin.services;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class AmazonS3ClientService {
	
	@Value("${application.bucket.name}")
	private String bucketName;
	
	private S3Client s3;
	
	public AmazonS3ClientService(S3Client s3) {
		this.s3 = s3;
	}
	
	public void saveText(String objectName, String text) {
		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectName)
                .build();
		
		s3.putObject(putObjectRequest, RequestBody.fromBytes(text.getBytes()));	
	}
	
	public void saveFile(MultipartFile file, String filename) {
		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .build();
		try {
			s3.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}
	
	public String getText(String objectName) {
		GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectName)
                .build();
		var response = s3.getObject(getObjectRequest);
		try {
			return new String(response.readAllBytes());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public byte[] getFileContent(String filename) {
		GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .build();
		var response = s3.getObject(getObjectRequest);
		try {
			return response.readAllBytes();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void delete(String objectName) {
		DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(objectName)
                .build();

        s3.deleteObject(deleteObjectRequest);
	}
}
