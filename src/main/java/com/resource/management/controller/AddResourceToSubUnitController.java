package com.resource.management.controller;

import com.resource.management.api.SubUnitUpdatedNotification;
import com.resource.management.api.edit.AddResourceToSubUnitRequest;
import com.resource.management.data.Resource;
import com.resource.management.data.SubUnit;
import com.resource.management.data.SubUnitsRepository;
import com.resource.management.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class AddResourceToSubUnitController {
    @Autowired
    private SubUnitsRepository subUnitsRepository;

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/addResource")
    public void handle(final AddResourceToSubUnitRequest request) {
        Optional<SubUnit> subUnitOptional = subUnitsRepository.findByName(request.getSubUnitName());
        if (subUnitOptional.isPresent()) {
            SubUnit subUnit = subUnitOptional.get();
            List<Resource> resources = subUnit.getResources();
            if (resources == null) {
                resources = new ArrayList<>();
            }

            resources.add(request.getResource());
            subUnit.setLastUpdate(Instant.now().toString());
            subUnitsRepository.save(subUnit);
            notificationService.publishSubUnitNotification(subUnit);
        }
    }
}
