package com.example.pastebin.controllers;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.pastebin.dtos.PostDetails;
import com.example.pastebin.dtos.PostDto;
import com.example.pastebin.entities.User;
import com.example.pastebin.services.HashKeyService;
import com.example.pastebin.services.PostService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class MainController {

	private PostService postService;
	private HashKeyService hashKeyService;
	
	@GetMapping
	public String mainPage(Model model) {
		model.addAttribute("postDto", new PostDto());
		return "main";
	}
	
	@PostMapping("/create")
	public String createTextBlock(@Valid PostDto postDto, Errors errors, Model model,
								  @AuthenticationPrincipal User user) {
		if (errors.hasErrors())
			return "main";
		
		String hashKey = postService.savePost(postDto, user);
		model.addAttribute("hashKey", hashKey);
		return "text_block_created";
	}
	
	@GetMapping("/get/{hashKey}")
	public String getPost(@PathVariable String hashKey, Model model) {
		Long id = hashKeyService.getIdFromHashKey(hashKey);
		PostDetails postDetails = postService.getPostDetailsById(id);
		model.addAttribute("postDetails", postDetails);
		return "show_text_block";
	}
	
	@GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        Resource resource = postService.getFileResource(filename);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }
}
