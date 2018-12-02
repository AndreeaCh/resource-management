/************************************************************************
 * * PROJECT:   XVP
 * * LANGUAGE:  Java, J2SE JDK 1.8
 * *
 * * COPYRIGHT: FREQUENTIS AG
 * *            Innovationsstrasse 1
 * *            A-1100 VIENNA
 * *            AUSTRIA
 * *            tel +43 1 811 50-0
 * *
 * * The copyright to the computer program(s) herein
 * * is the property of Frequentis AG, Austria.
 * * The program(s) shall not be used and/or copied without
 * * the written permission of Frequentis AG.
 * *
 ************************************************************************/
package com.resource.management.controller;

import com.resource.management.SubUnits;
import com.resource.management.api.ResourceStatus;
import com.resource.management.api.status.UpdateResourceStatusRequest;
import com.resource.management.model.SubUnit;
import com.resource.management.model.SubUnitMapper;
import com.resource.management.service.NotificationService;
import com.resource.management.service.SubUnitsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UpdateResourceStatusControllerTest {
    private static final String IP_ADDRESS = "12.2.2.2";

    @MockBean
    private SubUnitsService subUnitsService;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private UpdateResourceStatusController controller;

    @MockBean
    private SimpMessageHeaderAccessor headerAccessor;

    @Before
    public void setUp() throws Exception {
        Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put("ip", IP_ADDRESS);
        when(headerAccessor.getSessionAttributes()).thenReturn(sessionAttributes);

    }

    @Test
    public void handleRequest_itemInRepo_updatedItemIsSavedInRepo() {
        // given
        SubUnit subUnit = SubUnits.internal();
        final String plateNumber = subUnit.getResources().stream().findFirst().get().getPlateNumber();
        final ResourceStatus resourceStatus = ResourceStatus.IN_DECONTAMINATION;
        UpdateResourceStatusRequest request = new UpdateResourceStatusRequest(plateNumber, resourceStatus);

        // when
        controller.handle(request, headerAccessor);

        // then
        verify(subUnitsService).updateResourceStatus(request.getPlateNumber(), resourceStatus, IP_ADDRESS);
    }


    @Test
    public void handleRequest_itemInRepo_sendNotification() {
        // given
        SubUnit subUnit = SubUnits.internal();
        final String plateNumber = subUnit.getResources().stream().findFirst().get().getPlateNumber();
        ResourceStatus resourceStatus = ResourceStatus.AVAILABLE_IN_GARAGE;
        UpdateResourceStatusRequest request =
                new UpdateResourceStatusRequest(plateNumber, resourceStatus);
        when(subUnitsService.updateResourceStatus(request.getPlateNumber(), resourceStatus, IP_ADDRESS))
                .thenReturn(java.util.Optional.of(subUnit));

        // when
        controller.handle(request, headerAccessor);

        // then
        verify(notificationService).publishSubUnitNotification(SubUnitMapper.toApi(subUnit));
    }
}
