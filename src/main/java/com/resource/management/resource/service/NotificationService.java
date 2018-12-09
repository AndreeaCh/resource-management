package com.resource.management.resource.service;

import com.resource.management.api.resources.SubUnit;
import com.resource.management.api.resources.crud.notifications.SubUnitDeletedNotification;
import com.resource.management.api.resources.crud.notifications.SubUnitUpdatedNotification;
import com.resource.management.api.resources.lock.SubUnitLockedNotification;
import com.resource.management.api.resources.lock.SubUnitUnlockedNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

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
                "/topic/subunit",
                new SubUnitUpdatedNotification(subUnit)
        );
    }

    public void publishSubUnitDeletedNotification(final String name) {
        messagingTemplate.convertAndSend(
                "/topic/subunit",
                new SubUnitDeletedNotification(name)
        );
    }

    public void publishUnlockedSubUnitNotification(final String subUnitName) {
        messagingTemplate.convertAndSend(
                "/topic/unlockSubUnitNotification",
                new SubUnitUnlockedNotification(subUnitName)
        );
    }

    public void publishSubUnitLockedNotification(final String subUnitName) {
        messagingTemplate.convertAndSend(
                "/topic/lockSubUnitNotification",
                new SubUnitLockedNotification(subUnitName)
        );
    }
}