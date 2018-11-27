/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package com.resource.management.controller;

import com.resource.management.api.lock.SubUnitUnlockedNotification;
import com.resource.management.api.lock.UnlockSubUnitRequest;
import com.resource.management.model.SubUnit;
import com.resource.management.model.SubUnitsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class UnlockSubUnitController {

    private static final Logger LOG = LoggerFactory.getLogger(UnlockSubUnitController.class);

    @Autowired
    private SubUnitsRepository repository;

    @MessageMapping("/unlockSubUnit")
    @SendTo("/topic/unlockSubUnitNotification")
    public SubUnitUnlockedNotification handleUnlockSubUnitMessage(final UnlockSubUnitRequest request) {
        Optional<SubUnit> subUnit = repository.findByName(request.getSubUnitName());
        SubUnitUnlockedNotification notification = null;
        if (subUnit.isPresent()) {
            subUnit.get().setIsLocked(false);
            subUnit.get().setLockedBy(null);
            repository.save(subUnit.get());
            notification = new SubUnitUnlockedNotification(request.getSubUnitName());
        }

        return notification;
    }
}
