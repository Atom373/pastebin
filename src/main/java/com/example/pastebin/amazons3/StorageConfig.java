package com.example.pastebin.amazons3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import lombok.Data;

@Data
@Configuration
public class StorageConfig {
	
	@Value("${cloud.aws.credentials.access-key}")
	private String accessKey;
	
	@Value("${cloud.aws.credentials.secret-key}")
	private String secretKey;
	
	@Value("${cloud.aws.region.static}")
	private String region;
	
	@Value("${application.bucket.name}")
	private String bucketName;
	
	@Bean
	public AmazonS3 createS3Client() {
		return AmazonS3ClientBuilder
					.standard()
					.withCredentials(
							new AWSStaticCredentialsProvider(getCredentials()))
					.withRegion(region)
					.build();
	}
	
	private AWSCredentials getCredentials() {
        return new BasicAWSCredentials(
                accessKey,
                secretKey
        );
    }
}
