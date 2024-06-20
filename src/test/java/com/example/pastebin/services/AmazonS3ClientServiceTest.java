package com.example.pastebin.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AmazonS3ClientServiceTest {

	@Autowired
	private AmazonS3ClientService s3ClientService;
	
	private static String filename;
	private static byte[] content;
	private static MultipartFile multipartFile;
	private static String text;
	private static String objectName;
	
	@BeforeAll
	static void setUp() {
		text = "Some text";
		filename = "test.txt";
		content = text.getBytes();
		objectName = "name";
		multipartFile = new MockMultipartFile(
            "file",          
            filename,
            "text/plain",
            content
	    );
	}
	
	@Test
	void saveAndGetTextTest() {
		String textFromS3;
		
		s3ClientService.saveText(objectName, text);
		textFromS3 = s3ClientService.getText(objectName);
		
		assertEquals(text, textFromS3);
	}
	
	@Test
	void deleteTextTest() {
		s3ClientService.deleteObjectByName(objectName);
		
		assertThrows(NoSuchKeyException.class, () -> s3ClientService.getText(objectName));
	}
	
	@Test
	void saveAndGetFileTest() throws Exception {
		byte[] contentFromS3;
		
		s3ClientService.saveFile(multipartFile, filename);
		contentFromS3 = s3ClientService.getFileContent(filename);
		
		assertArrayEquals(content, contentFromS3);
	}
	
	@Test
	void deleteFileTest() {
		s3ClientService.deleteObjectByName(filename);
		
		assertThrows(NoSuchKeyException.class, () -> s3ClientService.getFileContent(filename));
	}

}
