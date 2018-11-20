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
import com.resource.management.api.status.UpdateResourceStatusRequest;
import com.resource.management.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class UpdateResourceStatusController {
    @Autowired
    private SubUnitsRepository repository;


    @MessageMapping("/updateStatus")
    @SendTo("/topic/unitUpdatedNotification")
    public SubUnitUpdatedNotification handle(@Payload final UpdateResourceStatusRequest request, final SimpMessageHeaderAccessor headerAccessor) {
        String ipAddress = headerAccessor.getSessionAttributes().get("ip").toString();

        SubUnitUpdatedNotification notification = null;
        List<SubUnit> subUnits = repository.findAll();
        final Optional<SubUnit> subUnitOptional = getSubUnitWithPlateNumber(subUnits, request.getPlateNumber());
        if (subUnitOptional.isPresent()) {
            SubUnit subUnit = subUnitOptional.get();
            updateResource(subUnit, request.getPlateNumber(), ipAddress, request.getResourceStatus());
            notification = new SubUnitUpdatedNotification(subUnit);
        }

        return notification;
    }

    private void updateResource(final SubUnit subUnit,
                                final String plateNumber,
                                final String ipAddress,
                                final ResourceStatus resourceStatus) {
        final Resource resource = getResourceWithPlateNumber(subUnit, plateNumber);
        resource.setStatus(resourceStatus);
        if (resource.getResourceLogs() == null) {
            resource.setResourceLogs(new ArrayList<>());
        }

        resource.getResourceLogs().add(new ResourceLog(UUID.randomUUID(), Instant.now().toString(), ipAddress, resourceStatus));
        repository.save(subUnit);
    }

    private Optional<SubUnit> getSubUnitWithPlateNumber(final List<SubUnit> subUnits, final String plateNumber) {
        return subUnits.stream()
                .filter(s -> s.getResources().stream().anyMatch(r -> r.getPlateNumber().equals(plateNumber)))
                .findFirst();
    }

    private Resource getResourceWithPlateNumber(final SubUnit subUnit, final String plateNumber) {
        return subUnit.getResources().stream().filter(resource -> resource.getPlateNumber().equals(plateNumber)).findFirst().orElse(null);
    }
}
