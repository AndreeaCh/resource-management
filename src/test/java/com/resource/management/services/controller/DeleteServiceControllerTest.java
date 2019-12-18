package com.resource.management.services.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
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
public class DeleteServiceControllerTest
{
   @MockBean
   private ServiceRepository repository;

   @MockBean
   private LastUpdatedTimestampRepository timestampRepository;

   @Autowired
   private DeleteServiceController sut;


   @Test
   public void contextLoads() throws Exception
   {
      assertThat( this.sut, notNullValue() );
   }


   @Test
   public void handleDeleteServiceRequest_sut_callsDeleteOnRepository()
   {
      //given
      final Service existingService = Services.api();
      final Service deletedService = Services.api();
      when( this.repository.findById( ArgumentMatchers.anyString() ) ).thenReturn( Optional.of( existingService ) );

      final DeleteServiceRequest request = new DeleteServiceRequest( deletedService.getId() );

      //when
      this.sut.handle( request );

      //then
      verify( this.repository ).deleteById( deletedService.getId().toString() );
   }


   @Test
   public void handleDeleteServiceRequest_sut_publishesNotification()
   {
      //given
      final Service existingService = Services.api();
      final Service deletedService = Services.api();
      when( this.repository.findAll() ).thenReturn( Collections.singletonList( existingService ) );

      final DeleteServiceRequest request = new DeleteServiceRequest( deletedService.getId() );

      //when
      final ServicesListUpdatedNotification notification = this.sut.handle( request );

      //then
      assertThat( "Expected service to be deleted.", notification.getServices(),
            containsInAnyOrder( existingService ) );
   }

   @Test
   public void handleDeleteServiceRequest_sut_callsSaveOnTimestampRepositoryAndPublishesTimestampInNotification()
   {
      //given
      final Service existingService = Services.api();
      final Service deletedService = Services.api();
      when( this.repository.findById( ArgumentMatchers.anyString() ) ).thenReturn( Optional.of( existingService ) );

      final DeleteServiceRequest request = new DeleteServiceRequest( deletedService.getId() );

      //when
      final LastUpdatedTimestamp timestamp = new LastUpdatedTimestamp( "timeStampToday", Instant.now().toString() );
      when( this.timestampRepository.saveTodaysTimestamp() ).thenReturn( timestamp );
      final ServicesListUpdatedNotification notification = this.sut.handle( request );

      //then
      assertThat( timestamp.getId(), equalTo( "timeStampToday" ) );
      assertThat( timestamp.getTimeStamp(), notNullValue() );
      assertThat( notification.getLastUpdateToday(), equalTo( timestamp.getTimeStamp() ) );
   }
}
