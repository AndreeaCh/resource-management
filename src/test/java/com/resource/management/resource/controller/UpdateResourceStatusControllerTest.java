package com.resource.management.resource.controller;

import com.resource.management.SubUnits;
import com.resource.management.api.resources.status.UpdateResourceStatusRequest;
import com.resource.management.resource.model.SubUnit;
import com.resource.management.resource.model.SubUnitMapper;
import com.resource.management.resource.model.ResourceStatus;
import com.resource.management.resource.service.NotificationService;
import com.resource.management.resource.service.SubUnitsService;
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
        final ResourceStatus resourceStatus = new ResourceStatus(ResourceStatus.Status.AVAILABLE);
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
        final ResourceStatus resourceStatus = new ResourceStatus(ResourceStatus.Status.AVAILABLE);
        UpdateResourceStatusRequest request = new UpdateResourceStatusRequest(plateNumber, resourceStatus);
        when(subUnitsService.updateResourceStatus(request.getPlateNumber(), resourceStatus, IP_ADDRESS))
                .thenReturn(java.util.Optional.of(subUnit));

        // when
        controller.handle(request, headerAccessor);

        // then
        verify(notificationService).publishSubUnitNotification(SubUnitMapper.toApi(subUnit));
    }
}
