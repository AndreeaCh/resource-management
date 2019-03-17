package com.resource.management.resource.controller;

import com.resource.management.api.management.subunits.AddSubUnitRequest;
import com.resource.management.api.management.subunits.AddSubUnitResponse;
import com.resource.management.api.resources.StatusCode;
import com.resource.management.resource.service.NotificationService;
import com.resource.management.resource.service.SubUnitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class AddSubUnitController {
    @Autowired
    private SubUnitsService service;

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/addSubUnit")
    @SendTo("/topic/unitAddedNotification")
    public AddSubUnitResponse handle(final AddSubUnitRequest request) {
        service.addSubUnit(request.getId(), request.getName());
        notificationService.publishInitialSubUnitsNotification();
        return new AddSubUnitResponse(StatusCode.OK);
    }
}
