package com.example.pastebin.services;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.pastebin.entities.MetaData;
import com.example.pastebin.entities.User;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class AmazonS3ClientService {
	
	@Value("${application.bucket.name}")
	private String bucketName;
	
	private final S3Client s3;
	
	public void saveText(String postName, String text) {
		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(postName)
                .build();
		
		s3.putObject(putObjectRequest, RequestBody.fromBytes(text.getBytes()));	
	}
	
	public void saveFiles(List<MultipartFile> files, String authorName) {
		files.stream()
			.filter( file -> !file.isEmpty() )
			.forEach( file -> saveFile(file, createFilename(file, authorName)) );
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
	
	public String getText(String postName) {
		GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(postName)
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
	
	public void deleteFilesByMeta(MetaData meta) {
		meta.getFilenames().stream().forEach( filename -> deleteObjectByName(filename));
	}
	
	public void deleteObjectByName(String objectName) {
		DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(objectName)
                .build();

        s3.deleteObject(deleteObjectRequest);
	}
	
	public String createFilename(MultipartFile file, String authorName) {
		return authorName + ":" + file.getOriginalFilename();
	}
}
