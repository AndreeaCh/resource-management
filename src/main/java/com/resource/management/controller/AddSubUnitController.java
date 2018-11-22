package com.resource.management.controller;

import com.resource.management.api.AddSubUnitRequest;
import com.resource.management.api.AddSubUnitResponse;
import com.resource.management.api.StatusCode;
import com.resource.management.api.SubUnitUpdatedNotification;
import com.resource.management.data.SubUnitsRepository;
import com.resource.management.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class AddSubUnitController {
    @Autowired
    private SubUnitsRepository repository;

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/addSubUnit")
    @SendTo("/topic/subunits")
    public AddSubUnitResponse handle(final AddSubUnitRequest request) {
        repository.insert(request.getSubUnit());
        notificationService.publishSubUnitAddedNotification(request.getSubUnit());
        return new AddSubUnitResponse(StatusCode.OK);
    }
}
