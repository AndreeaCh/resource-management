package com.resource.management.resource.controller;

import com.resource.management.SubUnits;
import com.resource.management.api.resources.lock.LockSubUnitRequest;
import com.resource.management.api.resources.lock.UnlockSubUnitRequest;
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

import java.util.*;

import static com.resource.management.ResourceTypes.randomResourceType;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UnlockSubUnitControllerTest {
    @MockBean
    private SubUnitsService subUnitsService;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private UnlockSubUnitController controller;

    @MockBean
    private SimpMessageHeaderAccessor accessor;

    @Test
    public void handleRequest_subUnitExists_sendSubUnitUnlockedNotification() {
        // Given
        SubUnit subUnit = SubUnits.internal();
        ResourceType resourceType = randomResourceType();
        when(subUnitsService.unlockSubUnit(eq(subUnit.getName()), eq(resourceType))).thenReturn(Optional.of(subUnit));

        // When
        controller.handleUnlockSubUnitMessage(new UnlockSubUnitRequest(subUnit.getName(), resourceType));

        // Then
        verify(notificationService).publishUnlockedSubUnitNotification(subUnit.getName(), new HashSet<>(Collections.singletonList(resourceType)));
    }

    @Test
    public void handleRequest_subUnitDoesNotExists_doesNotSedUnlockSubUnitNotification() {
        // Given
        SubUnit subUnit = SubUnits.internal();
        when(subUnitsService.unlockSubUnit(eq(subUnit.getName()), any())).thenReturn(Optional.empty());

        // When
        controller.handleUnlockSubUnitMessage(new UnlockSubUnitRequest(eq(subUnit.getName()), any()));

        // Then
        verifyNoMoreInteractions(notificationService);
    }
}