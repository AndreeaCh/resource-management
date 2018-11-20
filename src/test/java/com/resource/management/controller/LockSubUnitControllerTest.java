package com.resource.management.controller;

import com.resource.management.SubUnitsTestDataUtils;
import com.resource.management.api.edit.LockSubUnitRequest;
import com.resource.management.api.edit.SubUnitLockedNotification;
import com.resource.management.data.SubUnit;
import com.resource.management.data.SubUnitsRepository;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.test.context.junit4.SpringRunner;

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
        SubUnit subUnit = SubUnitsTestDataUtils.loadRandomSubUnit();
        when(subUnitsRepository.findByName(subUnit.getName())).thenReturn(Optional.of(subUnit));

        // When
        SubUnitLockedNotification notification = controller.handleLockSubUnitMessage(new LockSubUnitRequest(subUnit.getName()), accessor);

        // Then
        assertThat(notification.getSubUnitName(), is(subUnit.getName()));
    }

    @Test
    public void handleRequest_subUnitDoesNotExists_sendNullSubUnitLockedNotification() {
        // Given
        SubUnit subUnit = SubUnitsTestDataUtils.loadRandomSubUnit();
        when(subUnitsRepository.findByName(subUnit.getName())).thenReturn(Optional.empty());

        // When
        SubUnitLockedNotification notification = controller.handleLockSubUnitMessage(new LockSubUnitRequest(subUnit.getName()), accessor);

        // Then
        assertNull(notification);
    }
}