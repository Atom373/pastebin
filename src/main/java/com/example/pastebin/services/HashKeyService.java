package com.example.pastebin.services;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import org.springframework.stereotype.Service;

@Service
public class HashKeyService {
	
	private Encoder encoder = Base64.getEncoder();
	private Decoder decoder = Base64.getDecoder();
	
	public String getHashKeyFromId(Long id) {
		return encoder.encodeToString(String.valueOf(id).getBytes());
	}
	
	public Long getIdFromHashKey(String hashKey) {
		return Long.valueOf(new String(decoder.decode(hashKey)));
	}
}
