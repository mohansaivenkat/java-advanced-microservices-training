package com.rechargex.rechargeservice.repository;

import com.rechargex.rechargeservice.entity.RechargeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RechargeRepository extends JpaRepository<RechargeEntity, Long> {
    List<RechargeEntity> findByUserId(Long userId);
    Optional<RechargeEntity> findByTransactionId(String txnId);
}
