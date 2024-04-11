package com.example.pastebin.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.pastebin.entities.MetaData;
import com.example.pastebin.entities.User;

public interface MetaDataRepo extends CrudRepository<MetaData, Long>{
	
	List<MetaData> getAllByExpirationDateIsLessThan(Date currentDate);
	
	List<MetaData> getAllByAuthor(User user);
}
