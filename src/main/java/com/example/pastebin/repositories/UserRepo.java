package com.example.pastebin.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.pastebin.entities.User;

public interface UserRepo extends CrudRepository<User, Long>{
	
	User findByEmail(String email);

}
