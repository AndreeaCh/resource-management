package com.resource.management.resource.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.test.context.junit4.SpringRunner;

import com.resource.management.SubUnits;
import com.resource.management.api.resources.SubUnit;
import com.resource.management.api.resources.UpdateSubUnitRequest;
import com.resource.management.resource.model.SubUnitMapper;
import com.resource.management.resource.service.NotificationService;
import com.resource.management.resource.service.SubUnitsService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UpdateSubUnitControllerTest {
    private static final String IP_ADDRESS = "12.2.2.2";

    @MockBean
    private SubUnitsService subUnitService;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private UpdateSubUnitController controller;

    @MockBean
    private SimpMessageHeaderAccessor headerAccessor;


    @Before
    public void setUp() throws Exception {
        Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put("ip", IP_ADDRESS);
        String mockSessionId = UUID.randomUUID().toString();
        when(headerAccessor.getSessionId()).thenReturn(mockSessionId);
        when(headerAccessor.getSessionAttributes()).thenReturn(sessionAttributes);

    }

    @Test
    public void handleRequest_subUnitExists_sendUnitUpdatedNotification() {
        // Given
        when(subUnitService.updateSubUnit(SubUnits.updatedInternal(), IP_ADDRESS )).thenReturn(Optional.of(SubUnits.updatedInternal()));
        SubUnit updatedSubUnit = SubUnits.updatedApi();

        // When
        controller.handleUpdateSubUnitMessage(new UpdateSubUnitRequest(updatedSubUnit), headerAccessor);

        // Then
        verify(notificationService).publishSubUnitNotification(updatedSubUnit);
    }

    @Test
    public void handleRequest_subUnitExists_saveUpdatedSubUnit() {
        // Given
        when(subUnitService.updateSubUnit(SubUnits.updatedInternal(), IP_ADDRESS )).thenReturn(Optional.of(SubUnits.updatedInternal()));
        SubUnit updatedSubUnit = SubUnits.updatedApi();

        // When
        controller.handleUpdateSubUnitMessage(new UpdateSubUnitRequest(updatedSubUnit), headerAccessor);

        // Then
        verify(subUnitService).updateSubUnit(SubUnitMapper.toInternal(updatedSubUnit), IP_ADDRESS );
    }

    @Test
    public void handleRequest_subUnitDoesNotExist_doesNotSendUnitUpdatedNotification() {
        // Given
        when(subUnitService.updateSubUnit(SubUnits.updatedInternal(), IP_ADDRESS )).thenReturn(Optional.empty());
        SubUnit updatedSubUnit = SubUnits.updatedApi();

        // When
        controller.handleUpdateSubUnitMessage(new UpdateSubUnitRequest(updatedSubUnit), headerAccessor);

        // Then
        verifyNoMoreInteractions(notificationService);
    }
}
