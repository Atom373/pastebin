package com.example.pastebin.dtos;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.pastebin.annotations.Unique;
import com.example.pastebin.entities.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationForm {

	@NotBlank(message = "User name field can not be blank")
	private String username;
	
	@Email
	@NotBlank(message = "Email field can not be blank")
	@Unique(message = "This email is already in use")
	private String email;
	
	@Size(min=4, message="password must be at least 4 characters long")
	private String password;
	
	public User toUser(PasswordEncoder encoder) {
		return new User(username, email, encoder.encode(password));
	}
}
