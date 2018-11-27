/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package com.resource.management.controller;

import com.resource.management.api.lock.LockSubUnitRequest;
import com.resource.management.api.lock.SubUnitLockedNotification;
import com.resource.management.model.SubUnit;
import com.resource.management.model.SubUnitsRepository;
import com.resource.management.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class LockSubUnitController {
    private static final Logger LOG = LoggerFactory.getLogger(LockSubUnitController.class);
    @Autowired
    private SubUnitsRepository repository;

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/lockSubUnit")
    @SendTo("/topic/lockSubUnitNotification")
    public SubUnitLockedNotification handleLockSubUnitMessage(@Payload final LockSubUnitRequest request, final SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        Optional<SubUnit> subUnitOptional = repository.findByName(request.getSubUnitName());
        SubUnitLockedNotification notification = null;
        if (subUnitOptional.isPresent()) {
            SubUnit subUnit = subUnitOptional.get();
            lockSubUnit(sessionId, subUnit);
            repository.save(subUnit);
            notification = new SubUnitLockedNotification(request.getSubUnitName());
        }

        return notification;
    }

    private void lockSubUnit(final String sessionId, final SubUnit subUnit) {
        subUnit.setIsLocked(true);
        subUnit.setLockedBy(sessionId);
    }
}
