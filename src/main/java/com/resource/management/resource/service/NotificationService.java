package com.resource.management.resource.service;

import com.resource.management.api.resources.InitialSubUnitsNotification;
import com.resource.management.api.resources.LockedSubUnit;
import com.resource.management.api.resources.SubUnit;
import com.resource.management.api.resources.SubUnitUpdatedNotification;
import com.resource.management.api.resources.lock.SubUnitLockedNotification;
import com.resource.management.api.resources.lock.SubUnitUnlockedNotification;
import com.resource.management.resource.model.ResourceType;
import com.resource.management.resource.model.SubUnitMapper;
import com.resource.management.resource.model.SubUnitsRepository;
import com.resource.management.resource.model.configuration.SubUnitsConfiguration;
import com.resource.management.resource.model.configuration.SubUnitsConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private SubUnitsRepository repository;

    @Autowired
    private SubUnitsConfigurationRepository configurationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void publishSubUnitNotification(final SubUnit subUnit) {
        messagingTemplate.convertAndSend(
                "/topic/unitUpdatedNotification",
                new SubUnitUpdatedNotification(subUnit)
        );
    }

    public void publishUnlockedSubUnitNotification(final String subUnitId, final Set<ResourceType> resourceTypes) {
        messagingTemplate.convertAndSend(
                "/topic/unlockSubUnitNotification",
                new SubUnitUnlockedNotification(subUnitId, resourceTypes)
        );
    }

    public void publishSubUnitLockedNotification(final String subUnitId, final Set<ResourceType> resourceTypes) {
        messagingTemplate.convertAndSend(
                "/topic/lockSubUnitNotification",
                new SubUnitLockedNotification(subUnitId, resourceTypes)
        );
    }

    public void publishInitialSubUnitsNotification() {
        List<com.resource.management.resource.model.SubUnit> subUnits = getOrderedSubUnitsList();
        publishSubUnitsUpdatedNotification(subUnits);
    }

    public void publishSubUnitsUpdatedNotification(List<com.resource.management.resource.model.SubUnit> subUnits) {
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

    private List<com.resource.management.resource.model.SubUnit> getOrderedSubUnitsList() {
        List<com.resource.management.resource.model.SubUnit> subUnits = repository.findAll();
        List<com.resource.management.resource.model.SubUnit> orderedSubUnits = subUnits;
        Optional<SubUnitsConfiguration> subUnitsConfiguration = configurationRepository.findById(SubUnitsConfiguration.ID);
        if (subUnitsConfiguration.isPresent()) {
            orderedSubUnits = subUnits
                    .stream()
                    .sorted(subUnitsOrdering(subUnitsConfiguration.get().getOrderedSubUnitIds()))
                    .collect(Collectors.toList());
        }

        return orderedSubUnits;
    }

    private static Comparator<com.resource.management.resource.model.SubUnit> subUnitsOrdering(List<String> subUnitIds) {
        return Comparator.comparingInt(v -> subUnitIds.indexOf(v.getId()));
    }
}