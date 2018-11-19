package com.resource.management.controller;

import com.resource.management.api.AddSubUnitRequest;
import com.resource.management.api.SubUnitUpdatedNotification;
import com.resource.management.data.SubUnitsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class AddSubUnitController {
    @Autowired
    private SubUnitsRepository repository;

    @MessageMapping("/addSubUnit")
    @SendTo("/topic/subunits")
    public SubUnitUpdatedNotification handle(final AddSubUnitRequest request) {
        repository.insert(request.getSubUnit());
        return new SubUnitUpdatedNotification(request.getSubUnit());
    }
}
