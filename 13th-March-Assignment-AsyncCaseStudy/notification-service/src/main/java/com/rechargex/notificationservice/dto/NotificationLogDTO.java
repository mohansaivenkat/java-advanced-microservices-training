package com.rechargex.notificationservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationLogDTO {
    private Long rechargeId;
    private Long userId;
    private String type;
    private String status;
    private String message;
    private LocalDateTime sentAt;
}
