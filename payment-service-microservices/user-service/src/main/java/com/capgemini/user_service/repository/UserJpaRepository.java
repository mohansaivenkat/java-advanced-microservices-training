package com.capgemini.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capgemini.user_service.entity.UserInformation;

public interface UserJpaRepository extends JpaRepository<UserInformation, String>{

}
