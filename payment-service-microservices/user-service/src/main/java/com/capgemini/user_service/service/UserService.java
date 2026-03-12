package com.capgemini.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.user_service.entity.UserInformation;
import com.capgemini.user_service.repository.UserJpaRepository;

@Service
public class UserService {
	
	@Autowired
	private UserJpaRepository repo;

	public String createUser(UserInformation info) {
		// TODO Auto-generated method stub
		repo.save(info);
		return "created user";
	}

	public UserInformation getUser(String emailId) {
		// TODO Auto-generated method stub
		return repo.findById(emailId)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + emailId));
	}
	
	

}
