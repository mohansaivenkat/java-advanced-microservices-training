package com.capgemini.recharge.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.capgemini.recharge.config.RabbitConfig;
import com.capgemini.recharge.dto.RechargeProducerDto;

@Service
public class RechargeService {
	
	private RabbitTemplate rabbitTemplate;
	
	private Set<String> set = new HashSet<>();
	
	public RechargeService(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate=rabbitTemplate;
	}
	
	public String sendRechargeRequest(RechargeProducerDto request) {
		
		if(set.contains(request.getMobileNumber())) {
			return "Already request was done";
		}
		
		set.add(request.getMobileNumber());
		rabbitTemplate.convertAndSend(RabbitConfig.Queue_Name,request);
		
		return "message has been sent to broker";
	}

}
