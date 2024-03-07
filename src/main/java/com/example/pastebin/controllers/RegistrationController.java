package com.example.pastebin.controllers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.pastebin.repositories.UserRepo;
import com.example.pastebin.security.RegistrationForm;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/register")
@AllArgsConstructor
public class RegistrationController {

	private UserRepo userRepo;
	private PasswordEncoder passwordEncoder;
	
	@GetMapping
	public String registerForm(Model model) {
		model.addAttribute("registrationForm", new RegistrationForm());
		return "registration";
	}
	
	@PostMapping
	public String processRegistration(@Valid RegistrationForm form, Errors errors) {
		if (errors.hasErrors())
			return "registration";
		
		userRepo.save(form.toUser(passwordEncoder));
		return "redirect:/";
	}
}
