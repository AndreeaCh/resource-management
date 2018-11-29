package com.resource.management.controller;

import com.resource.management.api.crud.UpdateSubUnitRequest;
import com.resource.management.model.SubUnit;
import com.resource.management.model.SubUnitMapper;
import com.resource.management.service.NotificationService;
import com.resource.management.service.SubUnitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class UpdateSubUnitController {

    @Autowired
    private SubUnitsService subUnitService;

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/updateSubUnit")
    public void handleUpdateSubUnitMessage(final UpdateSubUnitRequest request) {
        Optional<SubUnit> updatedSubUnit = subUnitService.updateSubUnit(SubUnitMapper.toInternal(request.getSubUnit()));
        updatedSubUnit.ifPresent(s -> notificationService.publishSubUnitNotification(SubUnitMapper.toApi(s)));
    }
}
