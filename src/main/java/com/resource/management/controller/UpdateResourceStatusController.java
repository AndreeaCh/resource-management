/************************************************************************
 * * PROJECT:   XVP
 * * LANGUAGE:  Java, J2SE JDK 1.8
 * *
 * * COPYRIGHT: FREQUENTIS AG
 * *            Innovationsstrasse 1
 * *            A-1100 VIENNA
 * *            AUSTRIA
 * *            tel +43 1 811 50-0
 * *
 * * The copyright to the computer program(s) herein
 * * is the property of Frequentis AG, Austria.
 * * The program(s) shall not be used and/or copied without
 * * the written permission of Frequentis AG.
 * *
 ************************************************************************/
package com.resource.management.controller;

import com.resource.management.api.SubUnitUpdatedNotification;
import com.resource.management.api.UpdateResourceStatusRequest;
import com.resource.management.data.Resource;
import com.resource.management.data.ResourceLog;
import com.resource.management.data.SubUnit;
import com.resource.management.data.SubUnitsRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class UpdateResourceStatusController {
    @Autowired
    private SubUnitsRepository repository;


    @MessageMapping("/updatestatus")
    @SendTo("/topic/unitUpdatedNotification")
    public SubUnitUpdatedNotification handle(@Payload final UpdateResourceStatusRequest request, final SimpMessageHeaderAccessor headerAccessor) {
        String ipAddress = headerAccessor.getSessionAttributes().get("ip").toString();

        SubUnitUpdatedNotification notification = null;
        List<SubUnit> subUnits = repository.findAll();
        final Optional<Resource> resourceOptional =
                subUnits.stream()
                        .flatMap(subUnit -> subUnit.getResources().stream()
                                .filter(resource -> resource.getPlateNumber().equals(request.getPlateNumber())))
                        .findFirst();

        if (resourceOptional.isPresent()) {
            Resource resource = resourceOptional.get();
            resource.setStatus(request.getResourceStatus());

            if (resource.getResourceLogs() == null) {
                resource.setResourceLogs(new ArrayList<>());
            }

            resource.getResourceLogs().add(new ResourceLog(UUID.randomUUID(), Instant.now().toString(), ipAddress, request.getResourceStatus()));

            repository.saveAll(subUnits);

            final Optional<SubUnit> subUnit =
                    subUnits.stream()
                            .filter(s -> s.getResources().stream().anyMatch(r -> r.getPlateNumber().equals(request.getPlateNumber())))
                            .findFirst();
            notification = new SubUnitUpdatedNotification(subUnit.get());
        }

        return notification;
    }
}
