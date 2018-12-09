package com.resource.management.services.controller;

import com.resource.management.api.services.AddServiceRequest;
import com.resource.management.api.services.ServicesListUpdatedNotification;
import com.resource.management.services.Services;
import com.resource.management.services.model.Service;
import com.resource.management.services.model.ServiceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddServiceControllerTest {
    @MockBean
    private ServiceRepository repository;

    @Autowired
    private AddServiceController sut;

    @Test
    public void contextLoads() throws Exception {
        assertThat(this.sut, notNullValue());
    }

    @Test
    public void handleAddServiceRequest_sut_callsSaveOnRepository() {
        //given
        Service existingService = Services.api();
        Service addedService = Services.api();
        when(repository.findAll()).thenReturn(Arrays.asList(existingService, addedService));
        AddServiceRequest request = new AddServiceRequest(addedService);

        //when
        this.sut.handle(request);

        //then
        verify(repository).save(addedService);
    }

    @Test
    public void handleAddServiceRequest_sut_publishesNotification() {
        //given
        Service existingService = Services.api();
        Service addedService = Services.api();
        when(repository.findAll()).thenReturn(Arrays.asList(existingService, addedService));
        AddServiceRequest request = new AddServiceRequest(addedService);

        //when
        ServicesListUpdatedNotification notification = this.sut.handle(request);

        //then
        assertThat(
                "Expected service to be added.",
                notification.getServices(),
                containsInAnyOrder(existingService, addedService));
    }
}
