package com.resource.management.locations.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.resource.management.locations.model.LocationMapper;
import com.resource.management.locations.model.PointOfInterest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.resource.management.api.locations.AddLocationRequest;
import com.resource.management.api.locations.LocationsListUpdatedNotification;
import com.resource.management.locations.model.Location;
import com.resource.management.locations.model.LocationRepository;

@Controller
public class AddLocationController
{
   @Autowired
   private LocationRepository repository;


   @MessageMapping("/addLocation")
   @SendTo("/topic/locations")
   public LocationsListUpdatedNotification handle( final AddLocationRequest request )
   {
       List<PointOfInterest> points = request.getPointsOfInterest().stream().map(LocationMapper::toInternal).collect(Collectors.toList());
       final Location location =
            new Location( UUID.randomUUID().toString(), request.getName(), request.getCoordinates(), points
                  );
      this.repository.save( location );
      return new LocationsListUpdatedNotification( this.repository.findAll() );
   }
}
