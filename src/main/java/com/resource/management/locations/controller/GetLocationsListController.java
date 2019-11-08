package com.resource.management.locations.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import com.resource.management.api.locations.LocationsListUpdatedNotification;
import com.resource.management.locations.model.Location;
import com.resource.management.locations.model.LocationRepository;

@Controller
public class GetLocationsListController
{
   @Autowired
   private LocationRepository repository;


   @SubscribeMapping("/locations")
   public LocationsListUpdatedNotification handle()
   {
      final List<Location> locations = this.repository.findAll();

      return new LocationsListUpdatedNotification( locations );
   }
}
