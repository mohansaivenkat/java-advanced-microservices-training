package com.capgemini.recharge.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.recharge.dto.RechargeProducerDto;
import com.capgemini.recharge.service.RechargeService;

@RestController
public class RechargeController {
	
	private RechargeService service;

	public RechargeController(RechargeService service) {
		super();
		this.service = service;
	}
	
	@PostMapping("/recharge")
	public String doRecharge(@RequestBody RechargeProducerDto dto) {
		return service.sendRechargeRequest(dto);
	}
	

}
