/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package com.resource.management.controller;

import com.resource.management.api.crud.UpdateSubUnitRequest;
import com.resource.management.model.SubUnit;
import com.resource.management.model.SubUnitMapper;
import com.resource.management.model.SubUnitsRepository;
import com.resource.management.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.Optional;

@Controller
public class UpdateSubUnitController {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateSubUnitController.class);

    @Autowired
    private SubUnitsRepository repository;

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/updateSubUnit")
    public void handleUpdateSubUnitMessage(final UpdateSubUnitRequest request) {
        com.resource.management.api.SubUnit updatedSubUnit = request.getSubUnit();
        Optional<SubUnit> subUnit = repository.findByName(updatedSubUnit.getName());
        if (subUnit.isPresent() && !subUnit.get().equals(updatedSubUnit)) {
            updatedSubUnit.setLastUpdate(Instant.now().toString());
            repository.save(SubUnitMapper.toInternal(updatedSubUnit));
            notificationService.publishSubUnitNotification(updatedSubUnit);
        }
    }
}
