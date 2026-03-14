package com.rechargex.rechargeservice.service;

import com.rechargex.rechargeservice.client.OperatorServiceClient;
import com.rechargex.rechargeservice.client.PaymentServiceClient;
import com.rechargex.rechargeservice.config.RabbitMQConfig;
import com.rechargex.rechargeservice.dto.RechargeRequestDTO;
import com.rechargex.rechargeservice.dto.RechargeResponseDTO;
import com.rechargex.rechargeservice.dto.RechargeSuccessEvent;
import com.rechargex.rechargeservice.entity.RechargeEntity;
import com.rechargex.rechargeservice.entity.enums.RechargeStatus;
import com.rechargex.rechargeservice.exception.PaymentFailedException;
import com.rechargex.rechargeservice.exception.PlanNotFoundException;
import com.rechargex.rechargeservice.repository.RechargeRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RechargeService {

    private final RechargeRepository rechargeRepository;
    private final AmqpTemplate amqpTemplate;
    private final OperatorServiceClient operatorServiceClient;
    private final PaymentServiceClient paymentServiceClient;
    private final ApplicationEventPublisher eventPublisher;

    public RechargeService(RechargeRepository rechargeRepository, 
                           AmqpTemplate amqpTemplate, 
                           OperatorServiceClient operatorServiceClient, 
                           PaymentServiceClient paymentServiceClient,
                           ApplicationEventPublisher eventPublisher) {
        this.rechargeRepository = rechargeRepository;
        this.amqpTemplate = amqpTemplate;
        this.operatorServiceClient = operatorServiceClient;
        this.paymentServiceClient = paymentServiceClient;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public RechargeResponseDTO initiateRecharge(RechargeRequestDTO request, Long userId) {
        boolean validPlan = operatorServiceClient.validateOperatorAndPlan(request.getOperatorId(), request.getPlanId());
        if (!validPlan) {
            throw new PlanNotFoundException("Invalid operator or plan ID.");
        }

        BigDecimal planAmount = operatorServiceClient.getPlanAmount(request.getPlanId());

        boolean paymentSuccess = paymentServiceClient.processPayment(userId, planAmount, request.getPaymentMethod());
        if (!paymentSuccess) {
            throw new PaymentFailedException("Payment failed for the recharge.");
        }

        RechargeEntity entity = new RechargeEntity();
        entity.setUserId(userId);
        entity.setMobileNumber(request.getMobileNumber());
        entity.setOperatorId(request.getOperatorId());
        entity.setPlanId(request.getPlanId());
        entity.setAmount(planAmount);
        entity.setStatus(RechargeStatus.SUCCESS);
        entity.setTransactionId(UUID.randomUUID().toString());
        entity.setPaymentId(12345L);
        entity.setCompletedAt(LocalDateTime.now());
        entity.setEventPublished(false);
        
        entity = rechargeRepository.save(entity);

        RechargeSuccessEvent event = RechargeSuccessEvent.fromEntity(entity, "user" + userId + "@example.com");
        event.setOperatorName(operatorServiceClient.getOperatorName(request.getOperatorId()));
        event.setPlanName(operatorServiceClient.getPlanName(request.getPlanId()));
        event.setValidityDays(operatorServiceClient.getPlanValidity(request.getPlanId()));

        eventPublisher.publishEvent(event);

        RechargeResponseDTO responseDTO = new RechargeResponseDTO();
        responseDTO.setRechargeId(entity.getId());
        responseDTO.setTransactionId(entity.getTransactionId());
        responseDTO.setStatus("PROCESSING");
        responseDTO.setMessage("Recharge is processing asynchronously.");
        responseDTO.setAmount(entity.getAmount());
        responseDTO.setInitiatedAt(entity.getInitiatedAt() != null ? entity.getInitiatedAt() : LocalDateTime.now());

        return responseDTO;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRechargeSuccess(RechargeSuccessEvent event) {
        amqpTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, event);
    }
}
