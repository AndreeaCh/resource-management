package com.resource.management.controller;

import com.resource.management.api.StatusCode;
import com.resource.management.api.crud.AddSubUnitRequest;
import com.resource.management.api.crud.AddSubUnitResponse;
import com.resource.management.model.SubUnit;
import com.resource.management.model.SubUnitMapper;
import com.resource.management.model.SubUnitsRepository;
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
        SubUnit subUnit = SubUnitMapper.toInternal(request.getSubUnit());
        repository.insert(subUnit);
        notificationService.publishSubUnitAddedNotification(request.getSubUnit());
        return new AddSubUnitResponse(StatusCode.OK);
    }
}
