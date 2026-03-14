package com.rechargex.rechargeservice.dto;

import com.rechargex.rechargeservice.entity.RechargeEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RechargeSuccessEvent {

    @NotNull
    private Long rechargeId;

    @NotNull
    private Long userId;

    @NotBlank
    private String mobileNumber;

    @NotBlank
    private String operatorName;

    @NotBlank
    private String planName;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private Integer validityDays;

    @Email
    @NotBlank
    private String userEmail;

    @NotBlank
    private String transactionId;

    @NotNull
    private LocalDateTime rechargedAt;

    public static RechargeSuccessEvent fromEntity(RechargeEntity entity, String userEmail) {
        RechargeSuccessEvent event = new RechargeSuccessEvent();
        event.setRechargeId(entity.getId());
        event.setUserId(entity.getUserId());
        event.setMobileNumber(entity.getMobileNumber());
        event.setAmount(entity.getAmount());
        event.setUserEmail(userEmail);
        event.setTransactionId(entity.getTransactionId());
        event.setRechargedAt(entity.getCompletedAt() != null ? entity.getCompletedAt() : LocalDateTime.now());
        return event;
    }
}
