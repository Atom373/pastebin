package com.example.pastebin.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(force=true, access=AccessLevel.PRIVATE)
public class PostDetails {

	private final String text;
	private final String authorName;
}
