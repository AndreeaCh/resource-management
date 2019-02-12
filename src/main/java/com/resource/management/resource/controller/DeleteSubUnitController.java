package com.resource.management.resource.controller;

import com.resource.management.api.resources.StatusCode;
import com.resource.management.api.resources.crud.DeleteSubUnitRequest;
import com.resource.management.api.resources.crud.DeleteSubUnitResponse;
import com.resource.management.resource.service.NotificationService;
import com.resource.management.resource.service.SubUnitsService;
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
    @SendTo("/topic/unitDeletedNotification")
    public DeleteSubUnitResponse handle(final DeleteSubUnitRequest request) {
        service.deleteSubUnit(request.getName());
        notificationService.publishInitialSubUnitsNotification();
        return new DeleteSubUnitResponse(StatusCode.OK);
    }
}
