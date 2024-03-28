package com.example.pastebin.dtos;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.pastebin.entities.User;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostDto {
	
	@NotBlank(message = "You can not save an empty text block")
	private String text;
	
	private int lifetime;
	
	private List<MultipartFile> files;
	
	private String authorName;
	
}
