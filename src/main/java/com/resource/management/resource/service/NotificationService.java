package com.resource.management.resource.service;

import com.resource.management.api.resources.LockedSubUnit;
import com.resource.management.api.resources.SubUnit;
import com.resource.management.api.resources.crud.notifications.InitialSubUnitsNotification;
import com.resource.management.api.resources.crud.notifications.SubUnitDeletedNotification;
import com.resource.management.api.resources.crud.notifications.SubUnitUpdatedNotification;
import com.resource.management.api.resources.lock.SubUnitLockedNotification;
import com.resource.management.api.resources.lock.SubUnitUnlockedNotification;
import com.resource.management.resource.model.ResourceType;
import com.resource.management.resource.model.SubUnitMapper;
import com.resource.management.resource.model.SubUnitsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class NotificationService {
    @Autowired
    private SubUnitsRepository repository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void publishSubUnitNotification(final SubUnit subUnit) {
        messagingTemplate.convertAndSend(
                "/topic/unitUpdatedNotification",
                new SubUnitUpdatedNotification(subUnit)
        );
    }

    public void publishSubUnitAddedNotification(final SubUnit subUnit) {
        messagingTemplate.convertAndSend(
                "/topic/unitAddedNotification",
                new SubUnitUpdatedNotification(subUnit)
        );
    }

    public void publishSubUnitDeletedNotification(final String name) {
        messagingTemplate.convertAndSend(
                "/topic/unitDeletedNotification",
                new SubUnitDeletedNotification(name)
        );
    }

    public void publishUnlockedSubUnitNotification(final String subUnitName, final Set<ResourceType> resourceTypes) {
        messagingTemplate.convertAndSend(
                "/topic/unlockSubUnitNotification",
                new SubUnitUnlockedNotification(subUnitName, resourceTypes)
        );
    }

    public void publishSubUnitLockedNotification(final String subUnitName, final Set<ResourceType> resourceTypes) {
        messagingTemplate.convertAndSend(
                "/topic/lockSubUnitNotification",
                new SubUnitLockedNotification(subUnitName, resourceTypes)
        );
    }

    public void publishInitialSubUnitsNotification() {
        List<com.resource.management.resource.model.SubUnit> subUnits = repository.findAll();
        List<LockedSubUnit> lockedSubUnits = new ArrayList<>();
        subUnits.forEach(subUnit -> {
            if (subUnit.getLockedResourceTypeBySessionId() != null && !subUnit.getLockedResourceTypeBySessionId().isEmpty()) {
                LockedSubUnit lockedSubUnit = new LockedSubUnit(subUnit.getName(),
                        new HashSet<>(subUnit.getLockedResourceTypeBySessionId().values()));
                lockedSubUnits.add(lockedSubUnit);
            }
        });
        messagingTemplate.convertAndSend(
                "/topic/subunits",
                new InitialSubUnitsNotification(SubUnitMapper.toApi(subUnits), lockedSubUnits)
        );

    }
}