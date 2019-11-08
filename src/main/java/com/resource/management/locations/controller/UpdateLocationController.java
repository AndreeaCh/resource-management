package com.resource.management.locations.controller;

import java.util.ArrayList;
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
      LocationsListUpdatedNotification notification = null;
      if ( request.getLocation() != null )
      {
         final Location updatedLocation = LocationMapper.toInternal( request.getLocation() );

         final Optional<Location> locationOptional = this.repository.findById( updatedLocation.getId() );
         locationOptional.ifPresent( location ->
         {
            this.repository.save( updatedLocation );
         } );
         notification = new LocationsListUpdatedNotification( this.repository.findAll() );
      }
      else
      {
         notification = new LocationsListUpdatedNotification( new ArrayList() );
      }

      return notification;
   }
}
