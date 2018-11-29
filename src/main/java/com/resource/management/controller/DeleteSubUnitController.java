package com.resource.management.controller;

import com.resource.management.api.StatusCode;
import com.resource.management.api.crud.DeleteSubUnitRequest;
import com.resource.management.api.crud.DeleteSubUnitResponse;
import com.resource.management.service.NotificationService;
import com.resource.management.service.SubUnitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class DeleteSubUnitController {
    @Autowired
    private SubUnitsService service;

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/deleteSubUnit")
    @SendTo("/topic/subunits")
    public DeleteSubUnitResponse handle(final DeleteSubUnitRequest request) {
        service.deleteSubUnit(request.getName());
        notificationService.publishSubUnitDeletedNotification(request.getName());
        return new DeleteSubUnitResponse(StatusCode.OK);
    }
}
