package com.resource.management.resource.controller;

import java.util.Optional;

import com.resource.management.api.resources.StatusCode;
import com.resource.management.api.resources.UpdateSubUnitResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.resource.management.api.resources.UpdateSubUnitRequest;
import com.resource.management.resource.model.SubUnit;
import com.resource.management.resource.model.SubUnitMapper;
import com.resource.management.resource.service.NotificationService;
import com.resource.management.resource.service.SubUnitsService;

@Controller
public class UpdateSubUnitController {

    @Autowired
    private SubUnitsService subUnitService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/updateSubUnit")
    public void handleUpdateSubUnitMessage(@Payload final UpdateSubUnitRequest request,
                                           final SimpMessageHeaderAccessor headerAccessor) {
        UpdateSubUnitResponse response;
        String ipAddress = headerAccessor.getSessionAttributes().get("ip").toString();
        Optional<SubUnit> updatedSubUnit = subUnitService.updateSubUnit(SubUnitMapper.toInternal(request.getSubUnit()), ipAddress);
        if (updatedSubUnit.isPresent()) {
            notificationService.publishSubUnitNotification(SubUnitMapper.toApi(updatedSubUnit.get()));
            response = new UpdateSubUnitResponse(StatusCode.OK);
        } else {
            response = new UpdateSubUnitResponse(StatusCode.ERROR);
        }

        messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), "/queue/updateSubUnitResponse", response, headerAccessor.getMessageHeaders());
    }
}
