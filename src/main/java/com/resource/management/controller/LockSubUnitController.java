/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package com.resource.management.controller;

import com.resource.management.api.edit.LockSubUnitRequest;
import com.resource.management.api.edit.LockSubUnitResponse;
import com.resource.management.data.SubUnit;
import com.resource.management.data.SubUnitsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class LockSubUnitController {
    private static final Logger LOG = LoggerFactory.getLogger(LockSubUnitController.class);
    @Autowired
    private SubUnitsRepository repository;

    @SubscribeMapping("/locksubunit")
    public LockSubUnitResponse handleLockSubUnitMessage(final LockSubUnitRequest request) {
        SubUnit subUnit = repository.findByName(request.getSubUnitName());

        return new LockSubUnitResponse();
    }
}
