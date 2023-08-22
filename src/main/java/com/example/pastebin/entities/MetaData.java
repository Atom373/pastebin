package com.example.pastebin.entities;

import java.util.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(force=true, access=AccessLevel.PRIVATE)
public class MetaData { // represents meta data about objects in blob storage

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private final String objectName;
	private final Date expirationDate;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="user_details_id", nullable=false)
	private final User user;
}
