package com.example.pastebin.validators;

import com.example.pastebin.annotations.Unique;
import com.example.pastebin.repositories.UserRepo;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueValidator implements ConstraintValidator<Unique, String>{

	private UserRepo userRepo;
	
	public UniqueValidator(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		if (email == null)
			return false;
		return userRepo.findByEmail(email) == null;
	}

}
