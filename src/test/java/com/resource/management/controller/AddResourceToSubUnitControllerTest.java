package com.resource.management.controller;

import com.resource.management.ResourcesTestData;
import com.resource.management.SubUnits;
import com.resource.management.api.Resource;
import com.resource.management.api.crud.AddResourceToSubUnitRequest;
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
        final Resource newResource = ResourcesTestData.randomApi();
        final AddResourceToSubUnitRequest request = new AddResourceToSubUnitRequest(subUnit.getName(), newResource);

        //when
        sut.handle(request);

        //then
        verify(notificationService).publishSubUnitNotification(SubUnitMapper.toApi(subUnit));
    }

    private SubUnit prepareSubUnitInRepository() {
        SubUnit subUnit = SubUnits.internal();
        when(subUnitsRepository.findByName(subUnit.getName())).thenReturn(Optional.of(subUnit));
        return subUnit;
    }

}
