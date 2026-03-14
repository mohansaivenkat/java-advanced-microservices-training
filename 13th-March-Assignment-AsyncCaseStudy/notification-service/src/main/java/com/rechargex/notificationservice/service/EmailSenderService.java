package com.rechargex.notificationservice.service;

import com.rechargex.notificationservice.exception.NotificationException;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    public void send(String email, String subject, String body) throws NotificationException {
    }
}
