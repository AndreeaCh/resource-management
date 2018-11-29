package com.resource.management.controller;

import com.resource.management.SubUnits;
import com.resource.management.api.lock.LockSubUnitRequest;
import com.resource.management.model.SubUnit;
import com.resource.management.service.NotificationService;
import com.resource.management.service.SubUnitsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.test.context.junit4.SpringRunner;

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
        when(subUnitsService.lockSubUnit(eq(subUnit.getName()), any())).thenReturn(true);

        // When
        controller.handleLockSubUnitMessage(new LockSubUnitRequest(subUnit.getName()), accessor);

        // Then
        verify(notificationService).publishSubUnitLockedNotification(subUnit.getName());
    }

    @Test
    public void handleRequest_subUnitDoesNotExists_doesNotSendSubUnitLockedNotification() {
        // Given
        SubUnit subUnit = SubUnits.internal();
        when(subUnitsService.lockSubUnit(eq(subUnit.getName()), any())).thenReturn(false);

        // When
        controller.handleLockSubUnitMessage(new LockSubUnitRequest(subUnit.getName()), accessor);

        // Then
        verifyNoMoreInteractions(notificationService);
    }
}