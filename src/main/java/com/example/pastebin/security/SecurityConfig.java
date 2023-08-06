package com.example.pastebin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.pastebin.entities.User;
import com.example.pastebin.repositories.UserRepo;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService userDetailsService(UserRepo repo) {
		return email -> {
			User user = repo.findByEmail(email);
			if (user != null)
				return user;
			throw new UsernameNotFoundException("User with email " + email + " not found");
		};
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.authorizeHttpRequests( requests -> requests
				.requestMatchers("/").hasRole("USER")
				.anyRequest().permitAll()
			)
			.formLogin( form -> form
				.loginPage("/login")
				.defaultSuccessUrl("/")
				.permitAll()
				.usernameParameter("email")
			)
			.logout( logout -> logout
				.permitAll()
			)
			.build();
	}
}
