package com.example.pastebin.services;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
		User author = postDto.getAuthor();
		String title = getTitleOrDefault(postDto);
		
		List<String> filenames = getFilenames(postDto.getFiles(), author);
		MetaData meta = new MetaData(title, postName, getExpirationDate(lifetime), author, filenames);
		metaDataRepo.save(meta);
		return meta;
	}
	
	public MetaData getMetaById(long id) {
		return metaDataRepo
				.findById(id)
				.orElseThrow(() -> new NoSuchPostException());
	}
	
	public List<MetaData> getAllMetaByAuthor(User user) {
		return metaDataRepo.getAllByAuthor(user);
	}
	
	public MetaData deleteMetaById(long id) {
		MetaData meta = metaDataRepo.findById(id).get();
		metaDataRepo.delete(meta);
		return meta;
	}
	
	private List<String> getFilenames(List<MultipartFile> files, User author) {
		return files.stream()
				.filter( file -> !file.isEmpty())
				.map( file -> s3Client.createFilename(file, author.getUsername()) )
				.collect(Collectors.toList());
	}
	
	private String getTitleOrDefault(PostDto postDto) {
		if (postDto.getTitle().length() > 0)
			return postDto.getTitle();
		Date expirationDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd.MM.yyyy");
		return formatter.format(expirationDate);
	}
	
	private Date getExpirationDate(int lifetime) {
		return new Date(System.currentTimeMillis() + lifetime*60*1000);
	}
}
