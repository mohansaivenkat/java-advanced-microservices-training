package com.rechargex.notificationservice.consumer;

import com.rabbitmq.client.Channel;
import com.rechargex.notificationservice.dto.RechargeSuccessEvent;
import com.rechargex.notificationservice.entity.NotificationEntity;
import com.rechargex.notificationservice.exception.NotificationException;
import com.rechargex.notificationservice.repository.NotificationRepository;
import com.rechargex.notificationservice.service.EmailSenderService;
import com.rechargex.notificationservice.service.SmsSenderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RechargeEventConsumerTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private SmsSenderService smsSenderService;

    @Mock
    private EmailSenderService emailSenderService;

    @Mock
    private Channel channel;

    @InjectMocks
    private RechargeEventConsumer rechargeEventConsumer;

    @Test
    void testProcessRechargeSuccess_Success() throws Exception {
        RechargeSuccessEvent event = new RechargeSuccessEvent();
        event.setRechargeId(1L);
        event.setAmount(BigDecimal.TEN);
        event.setMobileNumber("1234567890");
        event.setUserEmail("user@example.com");

        when(notificationRepository.findByRechargeId(1L)).thenReturn(Collections.emptyList());

        rechargeEventConsumer.processRechargeSuccess(event, channel, 1L);

        verify(smsSenderService).send(eq("1234567890"), anyString());
        verify(emailSenderService).send(eq("user@example.com"), anyString(), anyString());
        verify(notificationRepository).save(any(NotificationEntity.class));
        verify(channel).basicAck(1L, false);
    }

    @Test
    void testProcessRechargeSuccess_Failure() throws Exception {
        RechargeSuccessEvent event = new RechargeSuccessEvent();
        event.setRechargeId(1L);
        event.setAmount(BigDecimal.TEN);
        event.setMobileNumber("1234567890");
        event.setUserEmail("user@example.com");

        when(notificationRepository.findByRechargeId(1L)).thenReturn(Collections.emptyList());
        
        doThrow(new NotificationException("SMS failed")).when(smsSenderService).send(anyString(), anyString());

        rechargeEventConsumer.processRechargeSuccess(event, channel, 1L);

        verify(channel).basicNack(1L, false, false);
        verify(channel, never()).basicAck(anyLong(), anyBoolean());
    }
}
