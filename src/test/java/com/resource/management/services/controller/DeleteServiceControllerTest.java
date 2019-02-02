package com.resource.management.services.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.resource.management.api.services.DeleteServiceRequest;
import com.resource.management.api.services.ServicesListUpdatedNotification;
import com.resource.management.services.Services;
import com.resource.management.services.model.LastUpdatedTimestamp;
import com.resource.management.services.model.LastUpdatedTimestampRepository;
import com.resource.management.services.model.Service;
import com.resource.management.services.model.ServiceRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeleteServiceControllerTest {
    @MockBean
    private ServiceRepository repository;

    @MockBean
    private LastUpdatedTimestampRepository timestampRepository;


    @Autowired
    private DeleteServiceController sut;

    @Test
    public void contextLoads() throws Exception {
        assertThat(this.sut, notNullValue());
    }

    @Test
    public void handleDeleteServiceRequest_sut_callsDeleteOnRepository() {
        //given
        Service existingService = Services.api();
        Service deletedService = Services.api();
        when(repository.findAll()).thenReturn(Collections.singletonList(existingService));

        DeleteServiceRequest request = new DeleteServiceRequest(deletedService.getId());

        //when
        this.sut.handle(request);

        //then
        verify(repository).deleteById(deletedService.getId().toString());
    }

    @Test
    public void handleDeleteServiceRequest_sut_publishesNotification() {
        //given
        Service existingService = Services.api();
        Service deletedService = Services.api();
        when(repository.findAll()).thenReturn(Collections.singletonList(existingService));

        DeleteServiceRequest request = new DeleteServiceRequest(deletedService.getId());

        //when
        ServicesListUpdatedNotification notification = this.sut.handle(request);

        //then
        assertThat(
                "Expected service to be deleted.",
                notification.getServices(),
                containsInAnyOrder(existingService));
    }

    @Test
    public void handleDeleteServiceRequest_sut_callsSaveOnTimestampRepositoryAndPublishesTimestampInNotification()
    {
        //given
        Service existingService = Services.api();
        Service deletedService = Services.api();
        when(repository.findAll()).thenReturn(Collections.singletonList(existingService));

        DeleteServiceRequest request = new DeleteServiceRequest(deletedService.getId());

        //when
        ServicesListUpdatedNotification notification = this.sut.handle(request);

        //then
        ArgumentCaptor<LastUpdatedTimestamp> captor = ArgumentCaptor.forClass( LastUpdatedTimestamp.class );
        verify( timestampRepository ).save( captor.capture() );
        LastUpdatedTimestamp timestamp = captor.getValue();
        assertThat( timestamp.getId(), equalTo( "timeStamp" ) );
        assertThat( timestamp.getTimeStamp(), notNullValue() );
        assertThat( notification.getLastUpdate(), equalTo( timestamp.getTimeStamp() ) );
    }
}
