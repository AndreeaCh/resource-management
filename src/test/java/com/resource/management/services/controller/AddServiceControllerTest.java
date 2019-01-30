package com.resource.management.services.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.resource.management.api.services.AddServiceRequest;
import com.resource.management.api.services.ServicesListUpdatedNotification;
import com.resource.management.services.Services;
import com.resource.management.services.model.LastUpdatedTimestamp;
import com.resource.management.services.model.LastUpdatedTimestampRepository;
import com.resource.management.services.model.Service;
import com.resource.management.services.model.ServiceRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddServiceControllerTest {
    @MockBean
    private ServiceRepository repository;

    @MockBean
    private LastUpdatedTimestampRepository timestampRepository;

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
        AddServiceRequest request
                = new AddServiceRequest(addedService.getName(), addedService.getTitle(), addedService.getRole(), addedService.getContact());

        //when
        this.sut.handle(request);

        //then
        ArgumentCaptor<Service> captor = ArgumentCaptor.forClass(Service.class);
        verify(repository).save(captor.capture());
        Service actualService = captor.getValue();
        assertThat(actualService.getName(), equalTo(addedService.getName()));
        assertThat(actualService.getTitle(), equalTo(addedService.getTitle()));
        assertThat(actualService.getRole(), equalTo(addedService.getRole()));
        assertThat(actualService.getContact(), equalTo(addedService.getContact()));
    }


    @Test
    public void handleAddServiceRequest_sut_publishesNotification() {
        //given
        Service existingService = Services.api();
        Service addedService = Services.api();
        when(repository.findAll()).thenReturn(Arrays.asList(existingService, addedService));
        AddServiceRequest request
                = new AddServiceRequest(addedService.getName(), addedService.getTitle(), addedService.getRole(), addedService.getContact());


        //when
        ServicesListUpdatedNotification notification = this.sut.handle(request);

        //then
        assertThat(
                "Expected service to be added.",
                notification.getServices(),
                containsInAnyOrder(existingService, addedService));
    }

    @Test
    public void handleAddServiceRequest_sut_callsSaveOnTimestampRepositoryAndPublishesTimestampInNotification()
    {
        //given
        Service existingService = Services.api();
        Service addedService = Services.api();
        when( repository.findAll() ).thenReturn( Arrays.asList( existingService, addedService ) );
        AddServiceRequest request
              = new AddServiceRequest( addedService.getName(), addedService.getTitle(), addedService.getRole(),
              addedService.getContact() );

        //when
        final ServicesListUpdatedNotification notification = this.sut.handle( request );

        //then
        ArgumentCaptor<LastUpdatedTimestamp> captor = ArgumentCaptor.forClass( LastUpdatedTimestamp.class );
        verify( timestampRepository ).save( captor.capture() );
        LastUpdatedTimestamp timestamp = captor.getValue();
        assertThat( timestamp.getId(), equalTo( "timeStamp" ) );
        assertThat( timestamp.getTimeStamp(), notNullValue() );
        assertThat( notification.getLastUpdate(), equalTo( timestamp.getTimeStamp() ) );
    }
}
