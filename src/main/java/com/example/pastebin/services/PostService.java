package com.example.pastebin.services;

import java.util.List;
import java.io.ByteArrayInputStream;
import java.util.Date;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.example.pastebin.dtos.PostDetails;
import com.example.pastebin.dtos.PostDto;
import com.example.pastebin.entities.MetaData;
import com.example.pastebin.entities.User;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostService {

	private AmazonS3ClientService s3Client;
	private MetaDataService metaDataService;
	private HashKeyService hashKeyService;
	
	public String savePost(PostDto postDto) {
		String postName = createPostName(postDto.getAuthorName());
		MetaData meta = metaDataService.createAndSaveMetaDataFor(postDto, postName);
	
		s3Client.saveText(postName, postDto.getText());
		s3Client.saveFiles(postDto.getFiles(), postDto.getAuthorName());
		
		return hashKeyService.getHashKeyFromId(meta.getId());
	}
	
	@Cacheable("PostDetails")
	public PostDetails getPostDetailsById(long id) {
		MetaData meta = metaDataService.getMetaById(id);
		return createPostDetails(meta);
	}
	
	public Resource getFileResource(String filename) {
		return new InputStreamResource(
						new ByteArrayInputStream(
								s3Client.getFileContent(filename)
						)
					);
	}
	
	@CacheEvict("PostDetails")
	public void deletePost(long id) {
		MetaData meta = metaDataService.deleteMetaById(id);
		String postName = meta.getPostName();
		s3Client.deleteFilesByMeta(meta);
		s3Client.deleteObjectByName(postName);
	}
	
	private PostDetails createPostDetails(MetaData metaData) {
		String postName = metaData.getPostName();
		String authorName = metaData.getAuthorName();
		List<String> filenames = metaData.getFilenames();
		return new PostDetails(s3Client.getText(postName), authorName, filenames);
	}

	private String createPostName(String authorName) {
		Date currentDate = new Date();
		return authorName + ":" + currentDate;
	}
	
}
