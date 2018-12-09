package com.resource.management.services.controller;

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

import java.util.Collections;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GetServicesListControllerTest {
    @MockBean
    private ServiceRepository repository;

    @Autowired
    private GetServicesListController sut;

    @Test
    public void contextLoads() throws Exception {
        assertThat(this.sut, notNullValue());
    }

    @Test
    public void handleGetServicesListRequest_sut_publishesNotification() {
        //given
        Service existingService = Services.api();
        when(repository.findAll()).thenReturn(Collections.singletonList(existingService));

        //when
        ServicesListUpdatedNotification notification = this.sut.handle();

        //then
        assertThat(
                "Expected services to be in there.",
                notification.getServices(),
                containsInAnyOrder(existingService));
    }
}
