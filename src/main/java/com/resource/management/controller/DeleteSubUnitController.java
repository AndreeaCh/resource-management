package com.resource.management.controller;

import com.resource.management.api.DeleteSubUnitRequest;
import com.resource.management.api.DeleteSubUnitResponse;
import com.resource.management.api.StatusCode;
import com.resource.management.api.SubUnitDeletedNotification;
import com.resource.management.data.SubUnitsRepository;
import com.resource.management.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class DeleteSubUnitController {
    @Autowired
    private SubUnitsRepository repository;

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/deleteSubUnit")
    @SendTo("/topic/subunits")
    public DeleteSubUnitResponse handle(final DeleteSubUnitRequest request) {
        repository.deleteById(request.getName());
        notificationService.publishSubUnitNotification(new SubUnitDeletedNotification(request.getName()));
        return new DeleteSubUnitResponse(StatusCode.OK);
    }
}
