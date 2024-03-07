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

import com.example.pastebin.entities.MetaData;
import com.example.pastebin.entities.PostDetails;
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
	
	@Cacheable("PostDetails")
	public PostDetails getPostDetails(String hashKey) {
		Long id = hashKeyService.getIdFromHashKey(hashKey);
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
		String objectName = meta.getObjectName();
		meta.getFilenames().stream().forEach( filename -> s3Client.delete(filename));
		metaDataRepo.deleteById(id);
		s3Client.delete(objectName);
	}
	
	private PostDetails createPostDetails(MetaData metaData) {
		String objectName = metaData.getObjectName();
		String username = metaData.getUser().getUsername();
		List<String> filenames = metaData.getFilenames();
		return new PostDetails(s3Client.getText(objectName), username, filenames);
	}

	public String savePost(User user, String text, 
								List<MultipartFile> files, int lifetime) {
		String objectName = createObjectName(user.getUsername());
		Long id = createMetaData(user, objectName, files, lifetime);
		s3Client.saveText(objectName, text);
		
		if (files != null)
			files.stream()
				.filter( file -> !file.isEmpty())
				.forEach( file -> s3Client.saveFile(file, createFilename(file, user)));
		
		return hashKeyService.getHashKeyFromId(id);
	}
	
	private Long createMetaData(User user, String objectName, 
								List<MultipartFile> files,int lifetime) {
		List<String> filenames = files.stream()
									.filter( file -> !file.isEmpty())
									.map( 
										file -> createFilename(file, user)
									).collect(Collectors.toList());
		MetaData meta = new MetaData(objectName, getExpirationDate(lifetime), filenames, user);
		meta = metaDataRepo.save(meta);
		return meta.getId();
	}

	private Date getExpirationDate(int lifetime) {
		return new Date(System.currentTimeMillis() + lifetime*60*1000);
	}
	
	private String createFilename(MultipartFile file, User user) {
		return user.getUsername() + ":" + file.getOriginalFilename();
	}

	private String createObjectName(String username) {
		Date currentDate = new Date();
		return username + ":" + currentDate;
	}
	
}
