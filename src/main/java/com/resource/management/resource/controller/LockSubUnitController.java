package com.resource.management.resource.controller;

import com.resource.management.api.resources.StatusCode;
import com.resource.management.api.resources.lock.LockSubUnitRequest;
import com.resource.management.api.resources.lock.LockSubUnitResponse;
import com.resource.management.resource.model.ResourceType;
import com.resource.management.resource.service.NotificationService;
import com.resource.management.resource.service.SubUnitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashSet;
import java.util.Map;

@Controller
public class LockSubUnitController {
    @Autowired
    private SubUnitsService service;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/lockSubUnit")
    public void handleLockSubUnitMessage(@Payload final LockSubUnitRequest request,
                                         final SimpMessageHeaderAccessor headerAccessor) {
        LockSubUnitResponse response;
        String sessionId = headerAccessor.getSessionId();
        Map<String, ResourceType> sessionIdResourceTypeMap = service.lockSubUnit(request.getSubUnitName(), request.getResourceType(), sessionId);
        if (sessionIdResourceTypeMap != null) {
            notificationService.publishSubUnitLockedNotification(request.getSubUnitName(), new HashSet<>(sessionIdResourceTypeMap.values()));
            response = new LockSubUnitResponse(StatusCode.OK);
        } else {
            response = new LockSubUnitResponse(StatusCode.ERROR);
        }

        messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), "/queue/lockSubUnitResponse", response, headerAccessor.getMessageHeaders());
    }
}
