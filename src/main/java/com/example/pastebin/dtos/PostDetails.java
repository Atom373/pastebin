package com.example.pastebin.dtos;

import java.io.Serializable;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(force=true, access=AccessLevel.PRIVATE)
public class PostDetails implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private final String title;
	private final String text;
	private final String authorName;
	private final List<String> filenames;
}
