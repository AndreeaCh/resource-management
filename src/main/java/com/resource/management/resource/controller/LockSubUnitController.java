package com.resource.management.resource.controller;

import com.resource.management.api.resources.lock.LockSubUnitRequest;
import com.resource.management.resource.model.ResourceType;
import com.resource.management.resource.service.NotificationService;
import com.resource.management.resource.service.SubUnitsService;
import java.util.HashSet;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class LockSubUnitController {
    @Autowired
    private SubUnitsService service;

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/lockSubUnit")
    public void handleLockSubUnitMessage(@Payload final LockSubUnitRequest request, final SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        Map<String, ResourceType> sessionIdResourceTypeMap = service.lockSubUnit(request.getSubUnitName(), request.getResourceType(), sessionId);
        if(sessionIdResourceTypeMap != null) {
            notificationService.publishSubUnitLockedNotification(request.getSubUnitName(), new HashSet<>(sessionIdResourceTypeMap.values()));
        }
    }
}
