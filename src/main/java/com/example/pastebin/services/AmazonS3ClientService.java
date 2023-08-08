package com.example.pastebin.services;

import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.example.pastebin.amazons3.StorageConfig;

@Service
public class AmazonS3ClientService {
	
	private AmazonS3 s3Client;
	private StorageConfig config;
	
	public AmazonS3ClientService(AmazonS3 s3Client) {
		this.s3Client = s3Client;
	}
	
	public void putObject(String objectName, String text) {
		s3Client.putObject(config.getBucketName(), objectName, text);
	}
	
	public void deleteObject(String objectName) {
		s3Client.deleteObject(config.getBucketName(), objectName);
	}
}
