package com.example.pastebin.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.pastebin.entities.User;

import lombok.Data;

@Data
public class RegistrationForm {

	private String username;
	private String email;
	private String password;
	
	public User toUser(PasswordEncoder encoder) {
		return new User(username, email, encoder.encode(password));
	}
}
