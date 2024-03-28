package com.example.pastebin.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(force=true, access=AccessLevel.PRIVATE)
public class MetaData implements Serializable { // represents meta data about objects in blob storage

	private static final long serialVersionUID = -4967133887662402044L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private final String postName;
	private final Date expirationDate;
	private final String authorName;
	
	@ElementCollection(fetch=FetchType.EAGER)
	private final List<String> filenames;
}
