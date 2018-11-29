package com.resource.management.controller;

import com.resource.management.api.status.UpdateResourceStatusRequest;
import com.resource.management.model.SubUnit;
import com.resource.management.model.SubUnitMapper;
import com.resource.management.service.NotificationService;
import com.resource.management.service.SubUnitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class UpdateResourceStatusController {

    @Autowired
    private SubUnitsService subUnitsService;

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/updateStatus")
    public void handle(@Payload final UpdateResourceStatusRequest request, final SimpMessageHeaderAccessor headerAccessor) {
        String ipAddress = headerAccessor.getSessionAttributes().get("ip").toString();
        Optional<SubUnit> subUnitOptional = subUnitsService.updateResourceStatus(request.getPlateNumber(), request.getResourceStatus(), ipAddress);
        subUnitOptional.ifPresent(subUnit -> notificationService.publishSubUnitNotification(SubUnitMapper.toApi(subUnit)));
    }
}
