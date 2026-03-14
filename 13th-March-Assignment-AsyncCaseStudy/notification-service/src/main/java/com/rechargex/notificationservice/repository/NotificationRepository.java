package com.rechargex.notificationservice.repository;

import com.rechargex.notificationservice.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByRechargeId(Long rechargeId);
    List<NotificationEntity> findByUserIdOrderBySentAtDesc(Long userId);
}
