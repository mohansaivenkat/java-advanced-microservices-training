package com.rechargex.rechargeservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "payment-service", url = "http://localhost:8084")
public interface PaymentServiceClient {
    @PostMapping("/api/v1/payments/process")
    boolean processPayment(@RequestParam("userId") Long userId, 
                           @RequestParam("amount") BigDecimal amount,
                           @RequestParam("method") String method);
}
