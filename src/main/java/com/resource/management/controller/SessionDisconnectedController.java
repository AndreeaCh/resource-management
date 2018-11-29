package com.resource.management.controller;

import com.resource.management.model.SubUnit;
import com.resource.management.service.NotificationService;
import com.resource.management.service.SubUnitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Optional;

@Controller
public class SessionDisconnectedController {
    @Autowired
    private SubUnitsService service;

    @Autowired
    private NotificationService notificationService;

    @EventListener
    public void onDisconnectEvent(final SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        Optional<SubUnit> subUnit = service.unlockSubUnitLockedBySession(sessionId);
        subUnit.ifPresent(s -> {
            notificationService.publishUnlockedSubUnitNotification(s.getName());
        });
    }
}
