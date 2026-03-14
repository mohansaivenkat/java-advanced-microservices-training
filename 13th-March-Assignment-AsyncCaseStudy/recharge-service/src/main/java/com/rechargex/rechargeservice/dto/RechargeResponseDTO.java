package com.rechargex.rechargeservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RechargeResponseDTO {
    private Long rechargeId;
    private String transactionId;
    private String status;
    private String message;
    private BigDecimal amount;
    private LocalDateTime initiatedAt;
}
