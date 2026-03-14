package com.rechargex.notificationservice.consumer;

import com.rabbitmq.client.Channel;
import com.rechargex.notificationservice.config.RabbitMQConfig;
import com.rechargex.notificationservice.dto.RechargeSuccessEvent;
import com.rechargex.notificationservice.entity.NotificationEntity;
import com.rechargex.notificationservice.entity.enums.NotifStatus;
import com.rechargex.notificationservice.entity.enums.NotifType;
import com.rechargex.notificationservice.repository.NotificationRepository;
import com.rechargex.notificationservice.service.EmailSenderService;
import com.rechargex.notificationservice.service.SmsSenderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RechargeEventConsumer {

    private final NotificationRepository notificationRepository;
    private final SmsSenderService smsSenderService;
    private final EmailSenderService emailSenderService;

    public RechargeEventConsumer(NotificationRepository notificationRepository,
                                 SmsSenderService smsSenderService,
                                 EmailSenderService emailSenderService) {
        this.notificationRepository = notificationRepository;
        this.smsSenderService = smsSenderService;
        this.emailSenderService = emailSenderService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME, containerFactory = "rabbitListenerContainerFactory")
    public void processRechargeSuccess(RechargeSuccessEvent event, Channel channel,
                                       @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws Exception {

        try {
            List<NotificationEntity> notifications = notificationRepository.findByRechargeId(event.getRechargeId());
            
            for (NotificationEntity entity : notifications) {
                if (entity.getStatus() == NotifStatus.SENT) {
                    channel.basicAck(deliveryTag, false);
                    return;
                }
            }

            String smsText = "Recharge of " + event.getAmount() + " is successful for " + event.getMobileNumber();
            smsSenderService.send(event.getMobileNumber(), smsText);
            
            String emailBody = "Dear User, your recharge of " + event.getAmount() + " is successful.";
            emailSenderService.send(event.getUserEmail(), "Recharge Success", emailBody);
            
            NotificationEntity notification = new NotificationEntity();
            notification.setRechargeId(event.getRechargeId());
            notification.setUserId(event.getUserId());
            notification.setMobileNumber(event.getMobileNumber());
            notification.setEmail(event.getUserEmail());
            notification.setType(NotifType.SMS);
            notification.setStatus(NotifStatus.SENT);
            notification.setMessage(smsText);
            
            notificationRepository.save(notification);
            
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            channel.basicNack(deliveryTag, false, false);
        }
    }
}
