/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package com.resource.management.controller;

import com.resource.management.api.edit.LockSubUnitRequest;
import com.resource.management.api.edit.SubUnitLockedNotification;
import com.resource.management.data.SubUnit;
import com.resource.management.data.SubUnitsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class LockSubUnitController {
    private static final Logger LOG = LoggerFactory.getLogger(LockSubUnitController.class);
    @Autowired
    private SubUnitsRepository repository;

    @MessageMapping("/locksubunit")
    @SendTo("/topic/subunits")
    public SubUnitLockedNotification handleLockSubUnitMessage(final LockSubUnitRequest request) {
        Optional<SubUnit> subUnit = repository.findByName(request.getSubUnitName());
        SubUnitLockedNotification notification = null;
        if (subUnit.isPresent()) {
            notification = new SubUnitLockedNotification(request.getSubUnitName());
        }

        return notification;
    }
}
