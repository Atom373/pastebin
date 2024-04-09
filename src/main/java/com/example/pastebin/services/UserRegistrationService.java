package com.example.pastebin.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.pastebin.repositories.UserRepo;
import com.example.pastebin.security.RegistrationForm;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserRegistrationService {

	private UserRepo userRepo;
	private PasswordEncoder passwordEncoder;
	//private AuthenticationManager authenticationManager;
	
	public void registerUser(RegistrationForm form) {
		userRepo.save(form.toUser(passwordEncoder));
		
		// Adding registered user into the security context
		UsernamePasswordAuthenticationToken authenticationToken =
	            new UsernamePasswordAuthenticationToken(
	            		form.getEmail(), form.getPassword(), Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
	            );
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	}
}
