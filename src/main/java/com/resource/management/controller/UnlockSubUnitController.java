package com.resource.management.controller;

import com.resource.management.api.lock.UnlockSubUnitRequest;
import com.resource.management.model.SubUnit;
import com.resource.management.service.NotificationService;
import com.resource.management.service.SubUnitsService;
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
        Optional<SubUnit> subUnit = subUnitsService.unlockSubUnit(request.getSubUnitName());
        subUnit.ifPresent(subUnit1 -> notificationService.publishUnlockedSubUnitNotification(subUnit1.getName()));
    }
}