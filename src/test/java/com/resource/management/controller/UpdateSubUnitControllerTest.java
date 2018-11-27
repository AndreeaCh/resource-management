package com.resource.management.controller;

import com.resource.management.SubUnits;
import com.resource.management.api.crud.UpdateSubUnitRequest;
import com.resource.management.model.SubUnit;
import com.resource.management.model.SubUnitMapper;
import com.resource.management.model.SubUnitsRepository;
import com.resource.management.service.NotificationService;
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
    private SubUnitsRepository subUnitsRepository;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private UpdateSubUnitController controller;


    @Test
    public void handleRequest_subUnitExists_sendUnitUpdatedNotification() {
        // Given
        prepareSubUnitInRepository();
        com.resource.management.api.SubUnit updatedSubUnit = SubUnits.updatedApi();

        // When
        controller.handleUpdateSubUnitMessage(new UpdateSubUnitRequest(updatedSubUnit));

        // Then
        verify(notificationService).publishSubUnitNotification(updatedSubUnit);
    }

    @Test
    public void handleRequest_subUnitExists_saveUpdatedSubUnit() {
        // Given
        prepareSubUnitInRepository();
        com.resource.management.api.SubUnit updatedSubUnit = SubUnits.updatedApi();

        // When
        controller.handleUpdateSubUnitMessage(new UpdateSubUnitRequest(updatedSubUnit));

        // Then
        verify(subUnitsRepository).save(SubUnitMapper.toInternal(updatedSubUnit));
    }

    @Test
    public void handleRequest_subUnitDoesNotExist_sendNullUnitUpdatedNotification() {
        // Given
        SubUnit subUnit = prepareSubUnitNotInRepository();

        // When
        com.resource.management.api.SubUnit apiSubUnit = SubUnitMapper.toApi(subUnit);
        controller.handleUpdateSubUnitMessage(new UpdateSubUnitRequest(apiSubUnit));

        // Then
        notificationService.publishSubUnitNotification(apiSubUnit);
    }

    @Test
    public void handleRequest_subUnitDoesNotExists_subUnitIsNotSaved() {
        // Given
        SubUnit subUnit = prepareSubUnitNotInRepository();

        // When
        com.resource.management.api.SubUnit apiSubUnit = SubUnitMapper.toApi(subUnit);
        controller.handleUpdateSubUnitMessage(new UpdateSubUnitRequest(apiSubUnit));

        // Then
        verify(subUnitsRepository, never()).save(subUnit);
    }

    private SubUnit prepareSubUnitInRepository() {
        SubUnit subUnit = SubUnits.internal();
        when(subUnitsRepository.findByName(subUnit.getName())).thenReturn(Optional.of(subUnit));
        return subUnit;
    }

    private SubUnit prepareSubUnitNotInRepository() {
        SubUnit subUnit = SubUnits.internal();
        when(subUnitsRepository.findByName(subUnit.getName())).thenReturn(Optional.empty());
        return subUnit;
    }
}