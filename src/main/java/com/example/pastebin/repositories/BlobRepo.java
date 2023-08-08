package com.example.pastebin.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.pastebin.entities.Blob;

public interface BlobRepo extends CrudRepository<Blob, Long>{

	List<Blob> getByUserId(long id);
}
