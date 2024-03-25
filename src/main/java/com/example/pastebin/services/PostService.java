package com.example.pastebin.services;

import java.util.List;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.pastebin.dtos.PostDetails;
import com.example.pastebin.dtos.PostDto;
import com.example.pastebin.entities.MetaData;
import com.example.pastebin.entities.User;
import com.example.pastebin.exceptions.NoSuchPostException;
import com.example.pastebin.repositories.MetaDataRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostService {

	private AmazonS3ClientService s3Client;
	private MetaDataRepo metaDataRepo;
	private HashKeyService hashKeyService;
	
	public String savePost(PostDto postDto, User user) {
		List<MultipartFile> files = postDto.getFiles();
		String text = postDto.getText();
		int lifetime = postDto.getLifetime();
		
		String postName = createPostName(user);
		MetaData meta = createMetaData(user, postName, files, lifetime);
		
		metaDataRepo.save(meta);
		s3Client.saveText(postName, text);
		s3Client.saveFiles(files, user);
		
		return hashKeyService.getHashKeyFromId(meta.getId());
	}
	
	@Cacheable("PostDetails")
	public PostDetails getPostDetailsById(@NonNull Long id) {
		MetaData metaData = metaDataRepo
								.findById(id)
								.orElseThrow(() -> new NoSuchPostException());
		return createPostDetails(metaData);
	}
	
	public Resource getFileResource(String filename) {
		return new InputStreamResource(
						new ByteArrayInputStream(
								s3Client.getFileContent(filename)
						)
					);
	}
	
	@CacheEvict("PostDetails")
	public void deletePost(@NonNull Long id) {
		MetaData meta = metaDataRepo.findById(id).get();
		String postName = meta.getPostName();
		s3Client.deleteFilesByMeta(meta);
		s3Client.deleteObject(postName);
		metaDataRepo.delete(meta);
	}
	
	private PostDetails createPostDetails(MetaData metaData) {
		String postName = metaData.getPostName();
		String username = metaData.getUser().getUsername();
		List<String> filenames = metaData.getFilenames();
		return new PostDetails(s3Client.getText(postName), username, filenames);
	}
	
	private MetaData createMetaData(User user, String postName, 
								List<MultipartFile> files, int lifetime) {
		List<String> filenames = getFilenames(files, user);
		MetaData meta = new MetaData(postName, getExpirationDate(lifetime), filenames, user);
		return meta;
	}
	
	private List<String> getFilenames(List<MultipartFile> files, User user) {
		return files.stream()
				.filter( file -> !file.isEmpty())
				.map( file -> createFilename(file, user) )
				.collect(Collectors.toList());
	}
	
	private Date getExpirationDate(int lifetime) {
		return new Date(System.currentTimeMillis() + lifetime*60*1000);
	}
	
	private String createFilename(MultipartFile file, User user) {
		return user.getUsername() + ":" + file.getOriginalFilename();
	}

	private String createPostName(User user) {
		Date currentDate = new Date();
		return user.getUsername() + ":" + currentDate;
	}
	
}
