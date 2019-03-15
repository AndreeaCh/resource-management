package com.resource.management.resource.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.resource.management.api.resources.UpdateSubUnitRequest;
import com.resource.management.resource.model.SubUnit;
import com.resource.management.resource.model.SubUnitMapper;
import com.resource.management.resource.service.NotificationService;
import com.resource.management.resource.service.SubUnitsService;

@Controller
public class UpdateSubUnitController {

    @Autowired
    private SubUnitsService subUnitService;

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/updateSubUnit")
    public void handleUpdateSubUnitMessage(final UpdateSubUnitRequest request, final SimpMessageHeaderAccessor headerAccessor) {
        String ipAddress = headerAccessor.getSessionAttributes().get("ip").toString();
        Optional<SubUnit> updatedSubUnit = subUnitService.updateSubUnit(SubUnitMapper.toInternal(request.getSubUnit()), ipAddress);
        updatedSubUnit.ifPresent(s -> notificationService.publishSubUnitNotification(SubUnitMapper.toApi(s)));
    }
}
