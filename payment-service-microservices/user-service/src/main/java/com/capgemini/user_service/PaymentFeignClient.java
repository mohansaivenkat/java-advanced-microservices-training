package com.capgemini.user_service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("PAYMENT-SERVICE")
public interface PaymentFeignClient {
	
	@GetMapping("/payment/options")
	public List<String> paymentOptions();

}
