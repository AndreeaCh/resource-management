package com.resource.management.resource.controller;

import com.resource.management.SubUnits;
import com.resource.management.api.resources.lock.LockSubUnitRequest;
import com.resource.management.resource.model.ResourceType;
import com.resource.management.resource.model.SubUnit;
import com.resource.management.resource.service.NotificationService;
import com.resource.management.resource.service.SubUnitsService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import static com.resource.management.ResourceTypes.randomResourceType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LockSubUnitControllerTest {

    @MockBean
    private SubUnitsService subUnitsService;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private LockSubUnitController controller;

    @MockBean
    private SimpMessageHeaderAccessor accessor;

    @Test
    public void handleRequest_subUnitExists_sendSubUnitLockedNotification() {
        // Given
        SubUnit subUnit = SubUnits.internal();
        ResourceType resourceType = randomResourceType();
        HashMap<String, ResourceType> resourceTypeLockedBySessionIdMap = getLockedResourceTypes(resourceType);
        when(subUnitsService.lockSubUnit(eq(subUnit.getName()), eq(resourceType), any())).thenReturn(resourceTypeLockedBySessionIdMap);

        // When
        controller.handleLockSubUnitMessage(new LockSubUnitRequest(subUnit.getName(), resourceType), accessor);

        // Then
        verify(notificationService).publishSubUnitLockedNotification(subUnit.getName(), new HashSet<>(Collections.singletonList(resourceType)));
    }

    @Test
    public void handleRequest_subUnitDoesNotExists_doesNotSendSubUnitLockedNotification() {
        // Given
        SubUnit subUnit = SubUnits.internal();
        when(subUnitsService.lockSubUnit(eq(subUnit.getName()), any(), any())).thenReturn(null);

        // When
        controller.handleLockSubUnitMessage(new LockSubUnitRequest(subUnit.getName(), randomResourceType()), accessor);
        // Then
        verifyNoMoreInteractions(notificationService);
    }

    private HashMap<String, ResourceType> getLockedResourceTypes(ResourceType resourceType) {
        HashMap<String, ResourceType> resourceTypeLockedBySessionIdMap = new HashMap<>();
        resourceTypeLockedBySessionIdMap.put(RandomStringUtils.randomAlphabetic(5), resourceType);
        return resourceTypeLockedBySessionIdMap;
    }
}