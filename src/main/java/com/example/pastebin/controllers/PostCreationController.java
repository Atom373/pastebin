package com.example.pastebin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pastebin.dtos.PostDto;
import com.example.pastebin.entities.User;
import com.example.pastebin.services.PostService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/")
@AllArgsConstructor 
public class PostCreationController {

	private PostService postService;
	
	@PostMapping("/create")
	public ResponseEntity<String> createTextBlock(PostDto postDto, Model model, 
												  @AuthenticationPrincipal User currentUser) {
		postDto.setAuthor(currentUser);
		String hashKey = postService.savePost(postDto);
		return ResponseEntity.ok().body(hashKey);
	}
}
