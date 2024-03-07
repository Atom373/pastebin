package com.example.pastebin.exceptionhandlers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.pastebin.exceptions.NoSuchPostException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NoSuchPostException.class)
	public String handleNoSuchPostException() {
		return "error_page";
	}
}
