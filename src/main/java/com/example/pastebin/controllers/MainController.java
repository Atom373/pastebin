package com.example.pastebin.controllers;

import java.util.Date;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.pastebin.entities.User;
import com.example.pastebin.repositories.TextBlockRepo;
import com.example.pastebin.services.AmazonS3ClientService;

@Controller
@RequestMapping("/")
public class MainController {

	private TextBlockRepo textBlockRepo;
	
	public MainController(TextBlockRepo textBlockRepo) {
		this.textBlockRepo = textBlockRepo;
	}
	
	@GetMapping
	public String mainPage() {
		return "main";
	}
	
	@PostMapping("/create")
	public String createTextBlock(@RequestParam(name = "text") String text,
								  @RequestParam(name = "lifetime") int lifetime,
								  @AuthenticationPrincipal User user) {
		textBlockRepo.saveTextBlock(user, text, lifetime);
		return "redirect:/";
	}
	
}
