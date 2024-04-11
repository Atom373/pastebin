package com.example.pastebin.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShortPostDetails {

	private String title;
	private String formattedExpirationDate;
}
