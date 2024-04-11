package com.example.pastebin.dtos;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.pastebin.entities.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostDto {
	
	@Size(max = 100, message = "the length of the title should not exceed 100 characters")
	private String title;
	
	@NotBlank(message = "You can not save an empty text block")
	private String text;
	
	private int lifetime;
	
	private List<MultipartFile> files;
	
	private User author;
}
