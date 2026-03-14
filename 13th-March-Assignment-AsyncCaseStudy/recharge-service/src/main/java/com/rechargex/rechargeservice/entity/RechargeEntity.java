package com.rechargex.rechargeservice.entity;

import com.rechargex.rechargeservice.entity.enums.RechargeStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "recharges")
public class RechargeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @NotBlank
    @Size(max = 10)
    private String mobileNumber;

    @Column(nullable = false)
    private Long operatorId;

    @Column(nullable = false)
    private Long planId;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private RechargeStatus status;

    @Column(unique = true)
    private String transactionId;

    @Column(nullable = true)
    private Long paymentId;

    @Column(nullable = true)
    private String failureReason;

    @CreationTimestamp
    private LocalDateTime initiatedAt;

    @Column(nullable = true)
    private LocalDateTime completedAt;

    @Column(columnDefinition = "boolean default false")
    private Boolean eventPublished = false;
}
