package com.resource.management.locations.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.resource.management.api.locations.LocationsListUpdatedNotification;
import com.resource.management.locations.model.Location;
import com.resource.management.locations.model.LocationRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GetLocationsListControllerTest
{
   @MockBean
   private LocationRepository repository;

   @Autowired
   private GetLocationsListController controller;


   @Test
   public void contextLoads() throws Exception
   {
      assertThat( this.controller, notNullValue() );
   }


   @Test
   public void handleGetLocationsListRequest_controller_publishesNotification()
   {
      //given
      final Location existingLocation = Locations.api();
      when( this.repository.findAll() ).thenReturn( Collections.singletonList( existingLocation ) );

      //when
      final LocationsListUpdatedNotification notification = this.controller.handle();

      //then
      assertThat( "Expected locations to be in there.", notification.getLocations(),
            containsInAnyOrder( existingLocation ) );
   }
}
