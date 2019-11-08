package com.resource.management.locations.controller;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.resource.management.api.locations.DeleteLocationRequest;
import com.resource.management.api.locations.LocationsListUpdatedNotification;
import com.resource.management.locations.model.Location;
import com.resource.management.locations.model.LocationRepository;

@Controller
public class DeleteLocationController
{
   @Autowired
   private LocationRepository repository;


   @MessageMapping("/deleteLocation")
   @SendTo("/topic/locations")
   public LocationsListUpdatedNotification handle( final DeleteLocationRequest request )
   {
      final Optional<Location> location = this.repository.findById( request.getId() );
      if ( location.isPresent() )
      {
         this.repository.deleteById( request.getId() );

         return new LocationsListUpdatedNotification( this.repository.findAll() );
      }

      return new LocationsListUpdatedNotification( new ArrayList() );
   }
}
