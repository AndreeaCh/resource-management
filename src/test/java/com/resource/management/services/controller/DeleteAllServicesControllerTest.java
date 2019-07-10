package com.resource.management.services.controller;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.resource.management.api.services.DeleteAllServicesRequest;
import com.resource.management.api.services.ServicesListUpdatedNotification;
import com.resource.management.services.model.LastUpdatedTimestamp;
import com.resource.management.services.model.LastUpdatedTimestampRepository;
import com.resource.management.services.model.ServiceRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeleteAllServicesControllerTest
{
   @MockBean
   private ServiceRepository repository;

   @MockBean
   private LastUpdatedTimestampRepository timestampRepository;

   @Autowired
   private DeleteAllServicesController sut;


   @Test
   public void contextLoads() throws Exception
   {
      assertThat( this.sut, notNullValue() );
   }


   @Test
   public void handleDeleteServiceRequest_sut_callsDeleteOnRepository()
   {
      //given
      when( this.repository.findAll() ).thenReturn( Collections.emptyList() );
      final String day = "TODAY";
      final DeleteAllServicesRequest request = new DeleteAllServicesRequest( day );

      //when
      this.sut.handle( request );

      //then
      verify( this.repository ).deleteByDay( day );
   }


   @Test
   public void handleDeleteServiceRequest_sut_publishesNotification()
   {
      //given
      when( this.repository.findAll() ).thenReturn( Collections.emptyList() );

      final DeleteAllServicesRequest request = new DeleteAllServicesRequest();

      //when
      final ServicesListUpdatedNotification notification = this.sut.handle( request );

      //then
      assertThat( "Expected service to be deleted.", notification.getServices(), empty() );
   }


   @Test
   public void handleDeleteServiceRequest_sut_callsSaveOnTimestampRepositoryAndPublishesTimestampInNotification()
   {
      //given
      when( this.repository.findAll() ).thenReturn( Collections.emptyList() );
      final DeleteAllServicesRequest request = new DeleteAllServicesRequest( "TODAY" );

      //when
      final ServicesListUpdatedNotification notification = this.sut.handle( request );
      final String now = Instant.now().toString();
      final LastUpdatedTimestamp timestamp = new LastUpdatedTimestamp( "timeStampToday", now );
      when( this.timestampRepository.saveTodaysTimestamp() ).thenReturn( timestamp );

      //then
      assertThat( timestamp.getId(), equalTo( "timeStampToday" ) );
      assertThat( timestamp.getTimeStamp(), notNullValue() );
      assertThat( notification.getLastUpdateToday(), equalTo( now ) );
   }
}
