package com.resource.management.services.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.resource.management.api.services.ServicesListUpdatedNotification;
import com.resource.management.services.Services;
import com.resource.management.services.model.LastUpdatedTimestamp;
import com.resource.management.services.model.LastUpdatedTimestampRepository;
import com.resource.management.services.model.Service;
import com.resource.management.services.model.ServiceRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GetServicesListControllerTest
{
   @MockBean
   private ServiceRepository repository;

   @MockBean
   private LastUpdatedTimestampRepository timestampRepository;

   @Autowired
   private GetServicesListController sut;


   @Test
   public void contextLoads() throws Exception
   {
      assertThat( this.sut, notNullValue() );
   }


   @Test
   public void handleGetServicesListRequest_sut_publishesNotification()
   {
      //given
      final Service existingService = Services.api();
      when( this.repository.findAll() ).thenReturn( Collections.singletonList( existingService ) );
      final String timeStamp = Instant.now().toString();
      when( this.timestampRepository.findById( "timeStampToday" ) )
            .thenReturn( Optional.of( new LastUpdatedTimestamp( "timeStampToday", timeStamp ) ) );

      //when
      final ServicesListUpdatedNotification notification = this.sut.handle();

      //then
      assertThat( "Expected services to be in there.", notification.getServices(),
            containsInAnyOrder( existingService ) );
      assertThat( "Expected timestamp.", notification.getLastUpdateToday(), equalTo( timeStamp ) );
   }
}
