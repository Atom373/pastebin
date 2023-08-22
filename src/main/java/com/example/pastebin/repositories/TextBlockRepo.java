package com.example.pastebin.repositories;

import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.pastebin.entities.MetaData;
import com.example.pastebin.entities.PostDetails;
import com.example.pastebin.entities.User;
import com.example.pastebin.services.AmazonS3ClientService;
import com.example.pastebin.services.HashKeyService;

@Repository
public class TextBlockRepo {

	private AmazonS3ClientService s3Client;
	private MetaDataRepo metaDataRepo;
	private HashKeyService hashKeyService; 
	
	public TextBlockRepo(AmazonS3ClientService s3Client, 
						 MetaDataRepo metaDataRepo,
						 HashKeyService hashKeyService) {
		this.s3Client = s3Client;
		this.metaDataRepo = metaDataRepo;
		this.hashKeyService = hashKeyService;
	}
	
	public PostDetails getPostDetailsOrNull(String hashKey) {
		Long id = hashKeyService.getIdFromHashKey(hashKey);
		Optional<MetaData> metaData = metaDataRepo.findById(id);
		if (metaData.isEmpty())
			return null;
		return createPostDetails(metaData.get());
	}
	
	private PostDetails createPostDetails(MetaData metaData) {
		String objectName = metaData.getObjectName();
		String username = metaData.getUser().getUsername();
		return new PostDetails(s3Client.getObject(objectName), username);
	}

	public String saveTextBlock(User user, String text, int lifetime) {
		String objectName = createObjectName(user.getUsername());
		Long id = createMetaData(user, objectName, lifetime);
		s3Client.putObject(objectName, text);
		return hashKeyService.getHashKeyFromId(id);
	}

	private String createObjectName(String username) {
		Date currentDate = new Date();
		return username + ":" + currentDate;
	}
	
	private Long createMetaData(User user, String objectName, int lifetime) {
		MetaData meta = new MetaData(objectName, getExpirationDate(lifetime), user);
		meta = metaDataRepo.save(meta);
		return meta.getId();
	}

	private Date getExpirationDate(int lifetime) {
		return new Date(System.currentTimeMillis() + lifetime*60*1000);
	}
	
	public void deleteTextBlock(Long id) {
		String objectName = metaDataRepo.findById(id).get().getObjectName();
		metaDataRepo.deleteById(id);
		s3Client.deleteObject(objectName);
	}
}
