package com.resource.management.resource.controller;

import com.resource.management.api.resources.lock.UnlockSubUnitRequest;
import com.resource.management.resource.model.SubUnit;
import com.resource.management.resource.service.NotificationService;
import com.resource.management.resource.service.SubUnitsService;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class UnlockSubUnitController {

    @Autowired
    private SubUnitsService subUnitsService;

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/unlockSubUnit")
    public void handleUnlockSubUnitMessage(final UnlockSubUnitRequest request) {
        Optional<SubUnit> subUnit = subUnitsService.unlockSubUnit(request.getSubUnitName(), request.getResourceType());
        subUnit.ifPresent(subUnit1 -> notificationService.publishUnlockedSubUnitNotification(
                subUnit1.getName(),
                new HashSet<>(subUnit1.getLockedResourceTypeBySessionId().values())));
    }
}