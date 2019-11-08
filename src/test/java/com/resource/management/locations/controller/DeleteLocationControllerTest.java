package com.resource.management.locations.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.resource.management.api.locations.DeleteLocationRequest;
import com.resource.management.api.locations.LocationsListUpdatedNotification;
import com.resource.management.locations.model.Location;
import com.resource.management.locations.model.LocationRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeleteLocationControllerTest
{
   @MockBean
   private LocationRepository repository;

   @Autowired
   private DeleteLocationController controller;


   @Test
   public void contextLoads() throws Exception
   {
      assertThat( this.controller, notNullValue() );
   }


   @Test
   public void handleDeleteLocationRequest_controller_callsDeleteOnRepository()
   {
      //given
      final Location existingLocation = Locations.api();
      final Location deletedLocation = Locations.api();
      when( this.repository.findById( ArgumentMatchers.anyString() ) ).thenReturn( Optional.of( existingLocation ) );

      final DeleteLocationRequest request = new DeleteLocationRequest( deletedLocation.getId() );

      //when
      this.controller.handle( request );

      //then
      verify( this.repository ).deleteById( deletedLocation.getId().toString() );
   }


   @Test
   public void handleDeleteLocationRequest_controller_publishesNotification()
   {
      //given
      final Location existingLocation = Locations.api();
      final Location deletedLocation = Locations.api();
      when( this.repository.findById( ArgumentMatchers.anyString() ) ).thenReturn( Optional.of( deletedLocation ) );
      when( this.repository.findAll() ).thenReturn( Collections.singletonList( existingLocation ) );

      final DeleteLocationRequest request = new DeleteLocationRequest( deletedLocation.getId() );

      //when
      final LocationsListUpdatedNotification notification = this.controller.handle( request );

      //then
      assertThat( "Expected location to be deleted.", notification.getLocations(),
            containsInAnyOrder( existingLocation ) );
   }

}
