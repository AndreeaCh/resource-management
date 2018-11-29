package com.resource.management.controller;

import com.resource.management.api.ResourceLog;
import com.resource.management.api.status.GetResourceLogRequest;
import com.resource.management.api.status.GetResourceLogResponse;
import com.resource.management.model.ResourceLogMapper;
import com.resource.management.service.SubUnitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GetResourceLogController {

    @Autowired
    private SubUnitsService service;

    @MessageMapping("/getResourceLog")
    @SendTo("/topic/resourceLogs")
    public GetResourceLogResponse handle(final GetResourceLogRequest request) {
        List<com.resource.management.model.ResourceLog> logs = service.getLogForResource(request.getPlateNumber());
        List<ResourceLog> resourceLogs = logs.stream().map(ResourceLogMapper::toApi).collect(Collectors.toList());
        return new GetResourceLogResponse(resourceLogs);
    }
}
