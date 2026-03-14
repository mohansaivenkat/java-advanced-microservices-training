package com.rechargex.rechargeservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "operator-service", url = "http://localhost:8083")
public interface OperatorServiceClient {
    @GetMapping("/api/v1/operators/{operatorId}/plans/{planId}/validate")
    boolean validateOperatorAndPlan(@PathVariable("operatorId") Long operatorId, @PathVariable("planId") Long planId);

    @GetMapping("/api/v1/operators/{operatorId}/name")
    String getOperatorName(@PathVariable("operatorId") Long operatorId);

    @GetMapping("/api/v1/plans/{planId}/name")
    String getPlanName(@PathVariable("planId") Long planId);

    @GetMapping("/api/v1/plans/{planId}/validity")
    Integer getPlanValidity(@PathVariable("planId") Long planId);

    @GetMapping("/api/v1/plans/{planId}/amount")
    java.math.BigDecimal getPlanAmount(@PathVariable("planId") Long planId);
}
