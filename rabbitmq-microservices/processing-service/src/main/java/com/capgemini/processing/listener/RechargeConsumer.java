package com.capgemini.processing.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.capgemini.processing.config.RabbitConfig;
import com.capgemini.processing.dto.RechargeDto;

@Component
public class RechargeConsumer {
	
	@RabbitListener(queues = RabbitConfig.Queue_Name)
	public void processRecharge(RechargeDto dto) throws InterruptedException {
		System.out.println("message received from queue");
		System.out.println("recharge processing for mobile "+ dto.getMobileNumber());
		Thread.sleep(5000);
		System.out.println("Recharge Done "+dto.getMobileNumber());
	}
}
