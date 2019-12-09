package com.resource.management.resource.controller;

import com.resource.management.api.resources.UpdateSubUnitRequest;
import com.resource.management.resource.model.SubUnit;
import com.resource.management.resource.model.SubUnitMapper;
import com.resource.management.resource.service.NotificationService;
import com.resource.management.resource.service.SubUnitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class UpdateSubUnitResponseController {
    @MessageMapping("/updateSubUnitResponse")
    public void handleUpdateSubUnitMessage() {
        //Just manage subscription
    }
}
