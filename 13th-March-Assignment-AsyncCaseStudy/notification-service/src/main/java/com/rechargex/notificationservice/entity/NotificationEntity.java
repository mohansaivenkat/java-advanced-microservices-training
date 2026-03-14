package com.rechargex.notificationservice.entity;

import com.rechargex.notificationservice.entity.enums.NotifStatus;
import com.rechargex.notificationservice.entity.enums.NotifType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notifications")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long rechargeId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = true)
    private String mobileNumber;

    @Column(nullable = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private NotifType type;

    @Enumerated(EnumType.STRING)
    private NotifStatus status;

    @Column(length = 500)
    private String message;

    @CreationTimestamp
    private LocalDateTime sentAt;

    @Column(nullable = true)
    private String failureReason;
}
