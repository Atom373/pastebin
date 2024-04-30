package com.example.pastebin.services;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class DateTimeService {

	private static String pattern = "HH:mm dd.MM.yyyy";
	private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
	private static SimpleDateFormat simpleDateFormatter = new SimpleDateFormat(pattern);
	
	public static String getFormattedCurrentDate() {
		LocalDateTime now = LocalDateTime.now();
		return now.format(dateTimeFormatter);
	}
	
	public static String formatDate(Date date) {
		return simpleDateFormatter.format(date);
	}
}
