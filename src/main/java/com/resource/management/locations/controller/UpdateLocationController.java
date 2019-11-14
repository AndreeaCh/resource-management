package com.resource.management.locations.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.resource.management.api.locations.LocationsListUpdatedNotification;
import com.resource.management.api.locations.UpdateLocationRequest;
import com.resource.management.locations.model.Location;
import com.resource.management.locations.model.LocationMapper;
import com.resource.management.locations.model.LocationRepository;

@Controller
public class UpdateLocationController
{
   @Autowired
   private LocationRepository repository;


   @MessageMapping("/updateLocation")
   @SendTo("/topic/locations")
   public LocationsListUpdatedNotification handle( final UpdateLocationRequest request )
   {
      final Optional<Location> locationOptional = this.repository.findById( request.getId() );

      locationOptional.ifPresent( location ->
      {
         final Location updatedLocation =
               LocationMapper.toInternal( new com.resource.management.api.locations.Location( request.getId(),
                     request.getName(), request.getCoordinates(), request.getPointsOfInterest() ) );
         this.repository.save( updatedLocation );
      } );

      return new LocationsListUpdatedNotification( this.repository.findAll() );
   }
}
