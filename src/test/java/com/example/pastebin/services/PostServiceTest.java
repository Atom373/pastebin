package com.example.pastebin.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PostServiceTest {

	@MockBean
	private AmazonS3ClientService s3Client;
	
	@Autowired
	private PostService postService;
	
	@BeforeAll
	static void setUp() {
		
	}
	
	@Test
	void test() {
		
	}

}
