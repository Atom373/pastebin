package com.example.pastebin.services;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.pastebin.dtos.PostDto;
import com.example.pastebin.entities.MetaData;
import com.example.pastebin.entities.User;
import com.example.pastebin.exceptions.NoSuchPostException;
import com.example.pastebin.repositories.MetaDataRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MetaDataService {

	private AmazonS3ClientService s3Client;
	private MetaDataRepo metaDataRepo;
	
	public MetaData createAndSaveMetaDataFor(PostDto postDto, String postName) {
		int lifetime = postDto.getLifetime();
		String authorName = postDto.getAuthorName();
		
		List<String> filenames = getFilenames(postDto.getFiles(), authorName);
		MetaData meta = new MetaData(postName, getExpirationDate(lifetime), authorName, filenames);
		metaDataRepo.save(meta);
		return meta;
	}
	
	public MetaData getMetaById(long id) {
		return metaDataRepo
				.findById(id)
				.orElseThrow(() -> new NoSuchPostException());
	}
	
	public MetaData deleteMetaById(long id) {
		MetaData meta = metaDataRepo.findById(id).get();
		metaDataRepo.delete(meta);
		return meta;
	}
	
	private List<String> getFilenames(List<MultipartFile> files, String authorName) {
		return files.stream()
				.filter( file -> !file.isEmpty())
				.map( file -> s3Client.createFilename(file, authorName) )
				.collect(Collectors.toList());
	}
	
	private Date getExpirationDate(int lifetime) {
		return new Date(System.currentTimeMillis() + lifetime*60*1000);
	}
}
