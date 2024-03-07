package com.example.pastebin.controllers;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.pastebin.entities.PostDetails;
import com.example.pastebin.entities.User;
import com.example.pastebin.services.PostService;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class MainController {

	private PostService postService;
	
	@GetMapping
	public String mainPage() {
		return "main";
	}
	
	@PostMapping("/create")
	public String createTextBlock(@RequestParam(name = "text") String text, Model model,
								  @RequestParam(name = "files") List<MultipartFile> files,
								  @RequestParam(name = "lifetime") int lifetime,
								  @AuthenticationPrincipal User user) {
		String hash = postService.savePost(user, text, files, lifetime);
		model.addAttribute("hashKey", hash);
		return "text_block_created";
	}
	
	@GetMapping("/get/{hashKey}")
	public String getTextBlock(@PathVariable String hashKey, Model model) {
		PostDetails postDetails = postService.getPostDetails(hashKey);
		model.addAttribute("postDetails", postDetails);
		return "show_text_block";
	}
	
	@GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        Resource resource = postService.getFileResource(filename);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }
}
