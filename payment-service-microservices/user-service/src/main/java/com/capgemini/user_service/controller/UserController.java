package com.capgemini.user_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.user_service.PaymentFeignClient;
import com.capgemini.user_service.entity.UserInformation;
import com.capgemini.user_service.service.UserService;

@RestController
public class UserController {

	private UserService service;
	
	@Autowired
	private PaymentFeignClient payment;

	public UserController(UserService service) {
		super();
		this.service = service;
	}

	@PostMapping("/create")
	public String create(@RequestBody UserInformation info) {
		return service.createUser(info);
	}

	@GetMapping("/{emailId}")
	public UserInformation getUser(@PathVariable String emailId) {
		return service.getUser(emailId);
	}

	@GetMapping("/hi")
	public String getUserTemp() {
		System.out.println("hi");
		return "hi";
	}
	
	
	@GetMapping("/pay")
	public List<String> getPayment(){
		return payment.paymentOptions();
	}
	
	
}
