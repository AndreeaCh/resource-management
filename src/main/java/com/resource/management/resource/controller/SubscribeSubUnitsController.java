package com.resource.management.resource.controller;

import com.resource.management.api.resources.LockedSubUnit;
import com.resource.management.api.resources.crud.notifications.InitialSubUnitsNotification;
import com.resource.management.resource.model.SubUnit;
import com.resource.management.resource.model.SubUnitMapper;
import com.resource.management.resource.model.SubUnitsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Controller
public class SubscribeSubUnitsController {

    @Autowired
    private SubUnitsRepository repository;

    @SubscribeMapping("/subunits")
    public InitialSubUnitsNotification handleSubscribeMessage() {
        List<SubUnit> subUnits = repository.findAll();
        List<LockedSubUnit> lockedSubUnits = new ArrayList<>();
        subUnits.forEach(subUnit -> {
            if (subUnit.getLockedResourceTypeBySessionId() != null && !subUnit.getLockedResourceTypeBySessionId().isEmpty()) {
                LockedSubUnit lockedSubUnit = new LockedSubUnit(subUnit.getName(),
                        new HashSet<>(subUnit.getLockedResourceTypeBySessionId().values()));
                lockedSubUnits.add(lockedSubUnit);
            }
        });
        return new InitialSubUnitsNotification(SubUnitMapper.toApi(subUnits), lockedSubUnits);
    }
}