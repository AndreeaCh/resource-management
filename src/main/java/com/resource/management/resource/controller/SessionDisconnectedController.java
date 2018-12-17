package com.resource.management.resource.controller;

import com.resource.management.resource.model.ResourceType;
import com.resource.management.resource.model.SubUnit;
import com.resource.management.resource.service.NotificationService;
import com.resource.management.resource.service.SubUnitsService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

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
            Map<String, ResourceType> lockedResourceBySessionIdMap = subUnit.get().getLockedResourceTypeBySessionId();
            notificationService.publishUnlockedSubUnitNotification(
                    s.getName(), lockedResourceBySessionIdMap != null ? new HashSet<>(lockedResourceBySessionIdMap.values()) : null);
        });
    }
}
