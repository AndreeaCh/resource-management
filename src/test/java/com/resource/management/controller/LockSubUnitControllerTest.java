package com.resource.management.controller;

import com.resource.management.SubUnits;
import com.resource.management.api.lock.LockSubUnitRequest;
import com.resource.management.api.lock.SubUnitLockedNotification;
import com.resource.management.model.SubUnit;
import com.resource.management.model.SubUnitsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LockSubUnitControllerTest {

    @MockBean
    private SubUnitsRepository subUnitsRepository;

    @Autowired
    private LockSubUnitController controller;

    @MockBean
    private SimpMessageHeaderAccessor accessor;

    @Test
    public void handleRequest_subUnitExists_sendSubUnitLockedNotification() {
        // Given
        SubUnit subUnit = SubUnits.internal();
        when(subUnitsRepository.findByName(subUnit.getName())).thenReturn(Optional.of(subUnit));

        // When
        SubUnitLockedNotification notification = controller.handleLockSubUnitMessage(new LockSubUnitRequest(subUnit.getName()), accessor);

        // Then
        assertThat(notification.getSubUnitName(), is(subUnit.getName()));
    }

    @Test
    public void handleRequest_subUnitDoesNotExists_sendNullSubUnitLockedNotification() {
        // Given
        SubUnit subUnit = SubUnits.internal();
        when(subUnitsRepository.findByName(subUnit.getName())).thenReturn(Optional.empty());

        // When
        SubUnitLockedNotification notification = controller.handleLockSubUnitMessage(new LockSubUnitRequest(subUnit.getName()), accessor);

        // Then
        assertNull(notification);
    }
}