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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.test.context.junit4.SpringRunner;

import com.resource.management.SubUnitsTestDataUtils;
import com.resource.management.api.SubUnitUpdatedNotification;
import com.resource.management.api.status.UpdateResourceStatusRequest;
import com.resource.management.data.ResourceStatus;
import com.resource.management.data.SubUnit;
import com.resource.management.data.SubUnitsRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UpdateResourceStatusControllerTest {
    @MockBean
    private SubUnitsRepository subUnitsRepository;

    @Autowired
    private UpdateResourceStatusController controller;

    @MockBean
    private SimpMessageHeaderAccessor headerAccessor;

    @Before
    public void setUp() throws Exception {
        Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put("ip", "12.2.2.2");
        when(headerAccessor.getSessionAttributes()).thenReturn(sessionAttributes);

    }

    @Test
    public void handleRequest_itemInRepo_updatedItemIsSavedInRepo() {
        // given
        final String plateNumber = prepareSubUnitWithResourceInRepository();
        final ResourceStatus resourceStatus = ResourceStatus.IN_DECONTAMINATION;
        UpdateResourceStatusRequest request = new UpdateResourceStatusRequest(plateNumber, resourceStatus);

        // when
        controller.handle(request, headerAccessor);

        // then
        ArgumentCaptor<SubUnit> captor = ArgumentCaptor.forClass(SubUnit.class);
        verify(subUnitsRepository).save(captor.capture());
        assertThat(captor.getValue().getResources().get(0).getStatus(), is(resourceStatus));
    }


    @Test
    public void handleRequest_itemInRepo_sendNotification() {
        // given
        final String plateNumber = prepareSubUnitWithResourceInRepository();
        UpdateResourceStatusRequest request =
                new UpdateResourceStatusRequest(plateNumber, ResourceStatus.AVAILABLE_IN_GARAGE);

        // when
        final SubUnitUpdatedNotification notification = controller.handle(request, headerAccessor);

        // then
        assertThat(notification.getSubUnit().getResources().get(0).getStatus(),
                is(ResourceStatus.AVAILABLE_IN_GARAGE));
    }


    private String prepareSubUnitWithResourceInRepository() {
        final SubUnit subUnit = SubUnitsTestDataUtils.loadRandomSubUnit();
        when(subUnitsRepository.findAll()).thenReturn(Collections.singletonList(subUnit));

        return subUnit.getResources().get(0).getPlateNumber();
    }
}
