/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package com.resource.management.controller;

import com.resource.management.api.SubUnitUpdatedNotification;
import com.resource.management.api.edit.UpdateSubUnitRequest;
import com.resource.management.data.SubUnit;
import com.resource.management.data.SubUnitsRepository;
import java.time.Instant;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class UpdateSubUnitController {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateSubUnitController.class);
    @Autowired
    private SubUnitsRepository repository;

    @MessageMapping("/updatesubunit")
    @SendTo("/topic/unitUpdatedNotification")
    public SubUnitUpdatedNotification handleUpdateSubUnitMessage(final UpdateSubUnitRequest request) {
        SubUnitUpdatedNotification notification = null;
        SubUnit updatedSubUnit = request.getSubUnit();
        Optional<SubUnit> subUnit = repository.findByName(updatedSubUnit.getName());
        if (subUnit.isPresent() && !subUnit.get().equals(updatedSubUnit)) {
            updatedSubUnit.setLastUpdate(Instant.now().toString());
            repository.save(updatedSubUnit);
            notification = new SubUnitUpdatedNotification(updatedSubUnit);
        }

        return notification;
    }
}
