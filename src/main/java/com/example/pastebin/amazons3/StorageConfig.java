package com.example.pastebin.amazons3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Data
@Configuration
public class StorageConfig {
	
	@Value("${aws.region}")
	private String region;
	
	@Bean
	public S3Client createS3Client() {
		return S3Client.builder()
	                .region(Region.of(region))
	                .build();
	}
	
}
