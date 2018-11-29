package com.resource.management.controller;

import com.resource.management.SubUnits;
import com.resource.management.api.crud.UpdateSubUnitRequest;
import com.resource.management.model.SubUnitMapper;
import com.resource.management.service.NotificationService;
import com.resource.management.service.SubUnitsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UpdateSubUnitControllerTest {

    @MockBean
    private SubUnitsService subUnitService;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private UpdateSubUnitController controller;


    @Test
    public void handleRequest_subUnitExists_sendUnitUpdatedNotification() {
        // Given
        when(subUnitService.updateSubUnit(SubUnits.updatedInternal())).thenReturn(Optional.of(SubUnits.updatedInternal()));
        com.resource.management.api.SubUnit updatedSubUnit = SubUnits.updatedApi();

        // When
        controller.handleUpdateSubUnitMessage(new UpdateSubUnitRequest(updatedSubUnit));

        // Then
        verify(notificationService).publishSubUnitNotification(updatedSubUnit);
    }

    @Test
    public void handleRequest_subUnitExists_saveUpdatedSubUnit() {
        // Given
        when(subUnitService.updateSubUnit(SubUnits.updatedInternal())).thenReturn(Optional.of(SubUnits.updatedInternal()));
        com.resource.management.api.SubUnit updatedSubUnit = SubUnits.updatedApi();

        // When
        controller.handleUpdateSubUnitMessage(new UpdateSubUnitRequest(updatedSubUnit));

        // Then
        verify(subUnitService).updateSubUnit(SubUnitMapper.toInternal(updatedSubUnit));
    }

    @Test
    public void handleRequest_subUnitDoesNotExist_doesNotSendUnitUpdatedNotification() {
        // Given
        when(subUnitService.updateSubUnit(SubUnits.updatedInternal())).thenReturn(Optional.empty());
        com.resource.management.api.SubUnit updatedSubUnit = SubUnits.updatedApi();

        // When
        controller.handleUpdateSubUnitMessage(new UpdateSubUnitRequest(updatedSubUnit));

        // Then
        verifyNoMoreInteractions(notificationService);
    }
}