package com.example.pastebin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
	public AuthenticationManager configureAuthenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
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
			.csrf().disable()
			.authorizeHttpRequests( requests -> requests
				.requestMatchers("/").authenticated()
				.anyRequest().permitAll()
			)
			.formLogin( form -> form
				.loginPage("/login")
				.defaultSuccessUrl("/", true)
				.permitAll()
				.usernameParameter("email")
			)
			.logout( logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login")
				.invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
			)
			.build();
	}
}
