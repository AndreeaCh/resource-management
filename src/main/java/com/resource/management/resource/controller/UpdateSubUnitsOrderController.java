package com.resource.management.resource.controller;

import com.resource.management.api.management.subunits.UpdateSubUnitsOrderRequest;
import com.resource.management.resource.model.SubUnit;
import com.resource.management.resource.service.NotificationService;
import com.resource.management.resource.service.SubUnitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UpdateSubUnitsOrderController {

    @Autowired
    private SubUnitsService subUnitService;

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/updateSubUnitsOrder")
    public void handleUpdateSubUnitMessage(final UpdateSubUnitsOrderRequest request) {
        List<SubUnit> subUnits = subUnitService.updateSubUnitsOrder(request.getSubUnitIds());
        notificationService.publishSubUnitsUpdatedNotification(subUnits);
    }
}
