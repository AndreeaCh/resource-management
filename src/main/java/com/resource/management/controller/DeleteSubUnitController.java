package com.resource.management.controller;

import com.resource.management.api.DeleteSubUnitRequest;
import com.resource.management.api.SubUnitDeletedNotification;
import com.resource.management.data.SubUnitsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class DeleteSubUnitController {
    @Autowired
    private SubUnitsRepository repository;

    @MessageMapping("/deleteSubUnit")
    @SendTo("/topic/subunits")
    public SubUnitDeletedNotification handle(final DeleteSubUnitRequest request) {
        repository.deleteById(request.getName());
        return new SubUnitDeletedNotification(request.getName());
    }
}
