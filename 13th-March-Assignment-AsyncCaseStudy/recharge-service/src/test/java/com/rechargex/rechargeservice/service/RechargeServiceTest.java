package com.rechargex.rechargeservice.service;

import com.rechargex.rechargeservice.client.OperatorServiceClient;
import com.rechargex.rechargeservice.client.PaymentServiceClient;
import com.rechargex.rechargeservice.config.RabbitMQConfig;
import com.rechargex.rechargeservice.dto.RechargeRequestDTO;
import com.rechargex.rechargeservice.dto.RechargeSuccessEvent;
import com.rechargex.rechargeservice.entity.RechargeEntity;
import com.rechargex.rechargeservice.exception.PaymentFailedException;
import com.rechargex.rechargeservice.exception.PlanNotFoundException;
import com.rechargex.rechargeservice.repository.RechargeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RechargeServiceTest {

    @Mock
    private RechargeRepository rechargeRepository;
    
    @Mock
    private AmqpTemplate amqpTemplate;
    
    @Mock
    private OperatorServiceClient operatorServiceClient;
    
    @Mock
    private PaymentServiceClient paymentServiceClient;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private RechargeService rechargeService;

    @Test
    void shouldInitiateRecharge_whenValidRequest() {
        RechargeRequestDTO req = new RechargeRequestDTO();
        req.setOperatorId(1L);
        req.setPlanId(1L);
        req.setPaymentMethod("UPI");
        req.setMobileNumber("1234567890");

        when(operatorServiceClient.validateOperatorAndPlan(1L, 1L)).thenReturn(true);
        when(operatorServiceClient.getPlanAmount(1L)).thenReturn(BigDecimal.TEN);
        when(paymentServiceClient.processPayment(eq(1L), any(), eq("UPI"))).thenReturn(true);
        when(rechargeRepository.save(any(RechargeEntity.class))).thenAnswer(i -> {
            RechargeEntity e = i.getArgument(0);
            e.setId(100L);
            return e;
        });

        rechargeService.initiateRecharge(req, 1L);

        verify(eventPublisher, times(1)).publishEvent(any(RechargeSuccessEvent.class));
        
        RechargeSuccessEvent event = new RechargeSuccessEvent();
        rechargeService.handleRechargeSuccess(event);
        verify(amqpTemplate, times(1)).convertAndSend(eq(RabbitMQConfig.EXCHANGE_NAME), eq(RabbitMQConfig.ROUTING_KEY), eq(event));
    }

    @Test
    void shouldThrowException_whenPlanInvalid() {
        RechargeRequestDTO req = new RechargeRequestDTO();
        req.setOperatorId(1L);
        req.setPlanId(1L);

        when(operatorServiceClient.validateOperatorAndPlan(1L, 1L)).thenReturn(false);

        assertThrows(PlanNotFoundException.class, () -> rechargeService.initiateRecharge(req, 1L));
        verify(amqpTemplate, never()).convertAndSend(anyString(), anyString(), any(Object.class));
    }

    @Test
    void shouldThrowException_whenPaymentFails() {
        RechargeRequestDTO req = new RechargeRequestDTO();
        req.setOperatorId(1L);
        req.setPlanId(1L);
        req.setPaymentMethod("UPI");

        when(operatorServiceClient.validateOperatorAndPlan(1L, 1L)).thenReturn(true);
        when(operatorServiceClient.getPlanAmount(1L)).thenReturn(BigDecimal.TEN);
        when(paymentServiceClient.processPayment(eq(1L), any(), eq("UPI"))).thenReturn(false);

        assertThrows(PaymentFailedException.class, () -> rechargeService.initiateRecharge(req, 1L));
        verify(amqpTemplate, never()).convertAndSend(anyString(), anyString(), any(Object.class));
    }
}
