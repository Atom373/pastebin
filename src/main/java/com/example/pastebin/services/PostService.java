package com.example.pastebin.services;

import java.util.List;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
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
		Long id = createMetaData(user, postName, files, lifetime);
		s3Client.saveText(postName, text);
		
		if (files != null)
			files.stream()
					.filter( file -> !file.isEmpty() )
					.forEach( file -> s3Client.saveFile(file, createFilename(file, user)) );
		
		return hashKeyService.getHashKeyFromId(id);
	}
	
	@Cacheable("PostDetails")
	public PostDetails getPostDetailsById(Long id) {
		Optional<MetaData> metaData = metaDataRepo.findById(id);
		if (metaData.isEmpty())
			throw new NoSuchPostException();
		return createPostDetails(metaData.get());
	}
	
	public Resource getFileResource(String filename) {
		return new InputStreamResource(
						new ByteArrayInputStream(
								s3Client.getFileContent(filename)
						)
					);
	}
	
	@CacheEvict("PostDetails")
	public void deletePost(Long id) {
		MetaData meta = metaDataRepo.findById(id).get();
		String postName = meta.getPostName();
		meta.getFilenames().stream().forEach( filename -> s3Client.delete(filename));
		metaDataRepo.deleteById(id);
		s3Client.delete(postName);
	}
	
	private PostDetails createPostDetails(MetaData metaData) {
		String postName = metaData.getPostName();
		String username = metaData.getUser().getUsername();
		List<String> filenames = metaData.getFilenames();
		return new PostDetails(s3Client.getText(postName), username, filenames);
	}
	
	private Long createMetaData(User user, String PostName, 
								List<MultipartFile> files,int lifetime) {
		List<String> filenames = files.stream()
									.filter( file -> !file.isEmpty())
									.map( 
										file -> createFilename(file, user)
									).collect(Collectors.toList());
		MetaData meta = new MetaData(PostName, getExpirationDate(lifetime), filenames, user);
		meta = metaDataRepo.save(meta);
		return meta.getId();
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
