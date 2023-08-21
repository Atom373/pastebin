package com.example.pastebin.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
	
	public void putObject(String objectName, String text) {
		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectName)
                .build();
		
		s3.putObject(putObjectRequest, RequestBody.fromBytes(text.getBytes()));
		
	}
	
	public String getObject(String objectName) {
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
	
	public void deleteObject(String objectName) {
		DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(objectName)
                .build();

        s3.deleteObject(deleteObjectRequest);
	}
}
