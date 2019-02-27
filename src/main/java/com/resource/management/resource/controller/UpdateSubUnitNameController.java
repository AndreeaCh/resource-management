package com.resource.management.resource.controller;

import com.resource.management.api.management.subunits.UpdateSubUnitNameRequest;
import com.resource.management.resource.model.SubUnit;
import com.resource.management.resource.model.SubUnitMapper;
import com.resource.management.resource.service.NotificationService;
import com.resource.management.resource.service.SubUnitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class UpdateSubUnitNameController {

    @Autowired
    private SubUnitsService subUnitService;

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/updateSubUnitName")
    public void handleUpdateSubUnitMessage(final UpdateSubUnitNameRequest request) {
        subUnitService.updateSubUnitName(request.getId(), request.getName());
        notificationService.publishInitialSubUnitsNotification();
    }
}
