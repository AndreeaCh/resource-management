package com.resource.management.controller;

import com.resource.management.SubUnitsTestDataUtils;
import com.resource.management.api.SubUnitUpdatedNotification;
import com.resource.management.api.edit.UpdateSubUnitRequest;
import com.resource.management.data.SubUnit;
import com.resource.management.data.SubUnitsRepository;

import java.util.Optional;

import com.resource.management.service.NotificationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        SubUnit updatedSubUnit = SubUnitsTestDataUtils.loadRandomSubUnitUpdate();

        // When
        controller.handleUpdateSubUnitMessage(new UpdateSubUnitRequest(updatedSubUnit));

        // Then
        verify(notificationService).publishSubUnitNotification(updatedSubUnit);
    }

    @Test
    public void handleRequest_subUnitExists_saveUpdatedSubUnit() {
        // Given
        prepareSubUnitInRepository();
        SubUnit updatedSubUnit = SubUnitsTestDataUtils.loadRandomSubUnitUpdate();

        // When
        controller.handleUpdateSubUnitMessage(new UpdateSubUnitRequest(updatedSubUnit));

        // Then
        verify(subUnitsRepository).save(updatedSubUnit);
    }

    @Test
    public void handleRequest_subUnitDoesNotExist_sendNullUnitUpdatedNotification() {
        // Given
        SubUnit subUnit = prepareSubUnitNotInRepository();

        // When
        controller.handleUpdateSubUnitMessage(new UpdateSubUnitRequest(subUnit));

        // Then
        notificationService.publishSubUnitNotification(subUnit);
    }

    @Test
    public void handleRequest_subUnitDoesNotExists_subUnitIsNotSaved() {
        // Given
        SubUnit subUnit = prepareSubUnitNotInRepository();

        // When
        controller.handleUpdateSubUnitMessage(new UpdateSubUnitRequest(subUnit));

        // Then
        verify(subUnitsRepository, never()).save(subUnit);
    }

    private SubUnit prepareSubUnitInRepository() {
        SubUnit subUnit = SubUnitsTestDataUtils.loadRandomSubUnit();
        when(subUnitsRepository.findByName(subUnit.getName())).thenReturn(Optional.of(subUnit));
        return subUnit;
    }

    private SubUnit prepareSubUnitNotInRepository() {
        SubUnit subUnit = SubUnitsTestDataUtils.loadRandomSubUnit();
        when(subUnitsRepository.findByName(subUnit.getName())).thenReturn(Optional.empty());
        return subUnit;
    }
}