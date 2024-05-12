package com.example.pastebin.services;

import java.util.List;
import java.util.stream.Collectors;
import java.io.ByteArrayInputStream;
import java.util.Date;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.example.pastebin.dtos.PostDetails;
import com.example.pastebin.dtos.PostDto;
import com.example.pastebin.dtos.ShortPostDetails;
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
		String postName = createPostName(postDto.getAuthor());
		MetaData meta = metaDataService.createAndSaveMetaDataFor(postDto, postName);
	
		s3Client.saveText(postName, postDto.getText().trim());
		s3Client.saveFiles(postDto.getFiles(), postDto.getAuthor());
		
		return hashKeyService.getHashKeyFromId(meta.getId());
	}
	
	@Cacheable("PostDetails")
	public PostDetails getPostDetailsById(long id) {
		MetaData meta = metaDataService.getMetaById(id);
		return createPostDetails(meta);
	}
	
	public List<ShortPostDetails> getAllUserPosts(User user) {
		return metaDataService
				.getAllMetaByAuthor(user)
				.stream()
				.map( meta -> createShortPostDetails(meta))
				.collect(Collectors.toList());
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
		String title = metaData.getTitle();
		String postName = metaData.getPostName();
		String authorName = metaData.getAuthor().getUsername();
		List<String> filenames = metaData.getFilenames();
		return new PostDetails(title, s3Client.getText(postName), authorName, filenames);
	}

	private ShortPostDetails createShortPostDetails(MetaData meta) {
		Date expirationDate = meta.getExpirationDate();
		String formattedExpirationDate = DateTimeService.formatDate(expirationDate);
		return new ShortPostDetails(meta.getTitle(), formattedExpirationDate);
	}
	
	private String createPostName(User author) {
		Date currentDate = new Date();
		return author.getUsername() + ":" + currentDate;
	}
	
}
