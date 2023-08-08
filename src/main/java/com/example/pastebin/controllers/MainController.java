package com.example.pastebin.controllers;

import java.util.Date;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.pastebin.entities.User;
import com.example.pastebin.services.AmazonS3ClientService;

@Controller
@RequestMapping("/")
public class MainController {

	private AmazonS3ClientService s3Client;
	
	public MainController(AmazonS3ClientService s3Client) {
		this.s3Client = s3Client;
	}
	
	@GetMapping
	public String mainPage() {
		return "main";
	}
	
	@PostMapping("/create")
	public String createTextBlock(@RequestParam(name = "text") String text,
								  @AuthenticationPrincipal User user) {
		String objectName = getObjectName(user.getUsername());
		s3Client.putObject(objectName, text);
		return "redirect:/";
	}
	
	private String getObjectName(String username) {
		Date currentDate = new Date();
		return username + currentDate;
	}
}
