package com.resource.management.locations.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.resource.management.locations.model.LocationMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.resource.management.api.locations.AddLocationRequest;
import com.resource.management.api.locations.LocationsListUpdatedNotification;
import com.resource.management.locations.model.Location;
import com.resource.management.locations.model.LocationRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddLocationControllerTest
{
   @MockBean
   private LocationRepository repository;

   @Autowired
   private AddLocationController controller;


   @Test
   public void contextLoads() {
      assertThat( this.controller, notNullValue() );
   }


   @Test
   public void handleAddLocationRequest_controller_callsSaveOnRepository()
   {
      //given
      final Location existingLocation = Locations.api();
      final Location addedLocation = Locations.api();
      when( this.repository.findAll() ).thenReturn( Arrays.asList( existingLocation, addedLocation ) );
      final AddLocationRequest request =
            new AddLocationRequest( addedLocation.getName(), addedLocation.getCoordinates(),
                    addedLocation.getPointsOfInterest().stream().map(LocationMapper::toApi).collect(Collectors.toList()));

      //when
      this.controller.handle( request );

      //then
      final ArgumentCaptor<Location> captor = ArgumentCaptor.forClass( Location.class );
      verify( this.repository ).save( captor.capture() );
      final Location actualLocation = captor.getValue();
      assertThat( actualLocation.getName(), equalTo( addedLocation.getName() ) );
      assertThat( actualLocation.getCoordinates(), equalTo( addedLocation.getCoordinates() ) );
      assertThat( actualLocation.getPointsOfInterest(), equalTo( addedLocation.getPointsOfInterest() ) );
   }


   @Test
   public void handleAddLocationRequest_controller_publishesNotification()
   {
      //given
      final Location existingLocation = Locations.api();
      final Location addedLocation = Locations.api();
      when( this.repository.findAll() ).thenReturn( Arrays.asList( existingLocation, addedLocation ) );
      final AddLocationRequest request =
            new AddLocationRequest( addedLocation.getName(), addedLocation.getCoordinates(),
                  addedLocation.getPointsOfInterest().stream().map(LocationMapper::toApi).collect(Collectors.toList()) );

      //when
      final LocationsListUpdatedNotification notification = this.controller.handle( request );

      //then
      assertThat( "Expected location to be added.", notification.getLocations(),
            containsInAnyOrder( existingLocation, addedLocation ) );
   }

}
