package com.example.pastebin.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.pastebin.entities.PostDetails;
import com.example.pastebin.entities.User;
import com.example.pastebin.services.HashKeyService;
import com.example.pastebin.services.TextBlockService;

@Controller
@RequestMapping("/")
public class MainController {

	private TextBlockService textBlockService;
	private HashKeyService hashKeyService;
	
	public MainController(TextBlockService textBlockService, HashKeyService hashKeyService) {
		this.textBlockService = textBlockService;
		this.hashKeyService = hashKeyService;
	}
	
	@GetMapping
	public String mainPage() {
		return "main";
	}
	
	@PostMapping("/create")
	public String createTextBlock(@RequestParam(name = "text") String text, Model model,
								  @RequestParam(name = "lifetime") int lifetime,
								  @AuthenticationPrincipal User user) {
		String hash = textBlockService.saveTextBlock(user, text, lifetime);
		model.addAttribute("hashKey", hash);
		return "text_block_created";
	}
	
	@GetMapping("/get/{hashKey}")
	public String getTextBlock(@PathVariable String hashKey, Model model) {
		Long id = hashKeyService.getIdFromHashKey(hashKey);
		PostDetails postDetails = textBlockService.getPostDetailsOrNull(id);
		if (postDetails == null)
			return "error_page";
		model.addAttribute("postDetails", postDetails);
		return "show_text_block";
	}
	
	
}
