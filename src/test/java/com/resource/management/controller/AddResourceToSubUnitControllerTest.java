package com.resource.management.controller;

import com.resource.management.ResourcesTestData;
import com.resource.management.SubUnitsTestDataUtils;
import com.resource.management.api.edit.AddResourceToSubUnitRequest;
import com.resource.management.data.Resource;
import com.resource.management.data.SubUnit;
import com.resource.management.data.SubUnitsRepository;
import com.resource.management.service.NotificationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddResourceToSubUnitControllerTest {
    @MockBean
    private SubUnitsRepository subUnitsRepository;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private AddResourceToSubUnitController sut;

    @Test
    public void contextLoads() throws Exception {
        assertThat(this.sut, notNullValue());
    }

    @Test
    public void handleRequest_sut_respondsWithSuccess() {
        //given
        final SubUnit subUnit = prepareSubUnitInRepository();
        final Resource newResource = ResourcesTestData.random();
        final AddResourceToSubUnitRequest request = new AddResourceToSubUnitRequest(subUnit.getName(), newResource);

        //when
        sut.handle(request);

        //then
        verify(notificationService).publishSubUnitNotification(subUnit);
    }

    private SubUnit prepareSubUnitInRepository() {
        SubUnit subUnit = SubUnitsTestDataUtils.loadRandomSubUnit();
        when(subUnitsRepository.findByName(subUnit.getName())).thenReturn(Optional.of(subUnit));
        return subUnit;
    }

}
