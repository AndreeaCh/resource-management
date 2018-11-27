/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package com.resource.management.controller;

import com.resource.management.api.lock.SubUnitUnlockedNotification;
import com.resource.management.model.SubUnit;
import com.resource.management.model.SubUnitsRepository;
import com.resource.management.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Optional;

@Controller
public class SessionDisconnectedController {
    @Autowired
    private SubUnitsRepository repository;

    @Autowired
    private NotificationService notificationService;

    @EventListener
    public void onDisconnectEvent(final SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        Optional<SubUnit> subUnit = repository.findByLockedBy(sessionId);
        subUnit.ifPresent(s -> {
            s.setLockedBy(null);
            repository.save(subUnit.get());
            notificationService.publishUnlockedSubUnitNotification(new SubUnitUnlockedNotification(s.getName()));
        });
    }
}
