package com.resource.management.resource.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.resource.management.api.management.subunits.DeleteSubUnitRequest;
import com.resource.management.api.management.subunits.DeleteSubUnitResponse;
import com.resource.management.api.resources.StatusCode;
import com.resource.management.resource.service.NotificationService;
import com.resource.management.resource.service.SubUnitsService;

@Controller
public class DeleteSubUnitController {
    @Autowired
    private SubUnitsService service;

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/deleteSubUnit")
    @SendTo("/topic/unitDeletedNotification")
    public DeleteSubUnitResponse handle(final DeleteSubUnitRequest request, final SimpMessageHeaderAccessor headerAccessor) {
        String ipAddress = headerAccessor.getSessionAttributes().get("ip").toString();
        service.deleteSubUnit(request.getId(), ipAddress);
        notificationService.publishInitialSubUnitsNotification();
        return new DeleteSubUnitResponse(StatusCode.OK);
    }
}
