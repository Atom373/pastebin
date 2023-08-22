package com.example.pastebin.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.pastebin.entities.MetaData;

public interface MetaDataRepo extends CrudRepository<MetaData, Long>{

	List<MetaData> getByUserId(long id);
}
