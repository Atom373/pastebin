package com.example.pastebin.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.pastebin.entities.MetaData;

public interface MetaDataRepo extends CrudRepository<MetaData, Long>{
	
	List<MetaData> findAllByExpirationDateIsLessThan(Date currentDate);
}
