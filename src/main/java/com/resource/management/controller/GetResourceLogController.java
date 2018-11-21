package com.resource.management.controller;

import com.resource.management.api.status.GetResourceLogRequest;
import com.resource.management.api.status.GetResourceLogResponse;
import com.resource.management.data.Resource;
import com.resource.management.data.SubUnit;
import com.resource.management.data.SubUnitsRepository;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GetResourceLogController {
    private static final Logger LOG = LoggerFactory.getLogger(GetResourceLogController.class);

    @Autowired
    private SubUnitsRepository repository;

    @MessageMapping("/getResourceLog")
    @SendTo("/topic/resourceLogs")
    public GetResourceLogResponse handle(final GetResourceLogRequest request) {
        GetResourceLogResponse response = new GetResourceLogResponse();
        List<SubUnit> subUnits = repository.findAll();
        final Optional<Resource> resourceOptional =
                subUnits.stream()
                        .flatMap(subUnit -> subUnit.getResources().stream().filter(resource -> resource.getPlateNumber().equals(request.getPlateNumber())))
                        .findFirst();
        if (resourceOptional.isPresent()) {
            response = new GetResourceLogResponse(resourceOptional.get().getResourceLogs());
        }

        return response;
    }
}
