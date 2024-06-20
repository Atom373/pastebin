package com.example.pastebin.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.pastebin.dtos.RegistrationForm;
import com.example.pastebin.services.UserRegistrationService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/register")
@AllArgsConstructor
public class RegistrationController {

	private UserRegistrationService userRegistrationService;
	
	@GetMapping
	public String registerForm(Model model) {
		model.addAttribute("registrationForm", new RegistrationForm());
		return "registration";
	}
	
	@PostMapping
	public String processRegistration(@Valid RegistrationForm form, Errors errors) {
		if (errors.hasErrors())
			return "registration";
		
		userRegistrationService.registerUser(form);
		return "redirect:/";
	}
}
