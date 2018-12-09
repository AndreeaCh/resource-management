package com.resource.management.services.controller;

import com.resource.management.api.services.DeleteAllServicesRequest;
import com.resource.management.api.services.ServicesListUpdatedNotification;
import com.resource.management.services.model.ServiceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeleteAllServicesControllerTest {
    @MockBean
    private ServiceRepository repository;

    @Autowired
    private DeleteAllServicesController sut;

    @Test
    public void contextLoads() throws Exception {
        assertThat(this.sut, notNullValue());
    }

    @Test
    public void handleDeleteServiceRequest_sut_callsDeleteOnRepository() {
        //given
        when(repository.findAll()).thenReturn(Collections.emptyList());
        DeleteAllServicesRequest request = new DeleteAllServicesRequest();

        //when
        this.sut.handle(request);

        //then
        verify(repository).deleteAll();
    }

    @Test
    public void handleDeleteServiceRequest_sut_publishesNotification() {
        //given
        when(repository.findAll()).thenReturn(Collections.emptyList());

        DeleteAllServicesRequest request = new DeleteAllServicesRequest();

        //when
        ServicesListUpdatedNotification notification = this.sut.handle(request);

        //then
        assertThat(
                "Expected service to be deleted.",
                notification.getServices(),
                empty());
    }
}
