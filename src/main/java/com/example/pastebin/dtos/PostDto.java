package com.example.pastebin.dtos;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.pastebin.entities.User;

import lombok.Data;

@Data
public class PostDto {
	
	private String title;
	
	private String text;
	
	private int lifetime;
	
	private List<MultipartFile> files;
	
	private User author;
}
