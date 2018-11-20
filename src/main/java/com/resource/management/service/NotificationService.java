package com.resource.management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void publishSubUnitNotification(final Object notification) {
        messagingTemplate.convertAndSend(
                "/topic/subunit",
                notification
        );
    }
}