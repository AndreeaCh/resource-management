package com.resource.management.services.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import com.resource.management.api.services.ServicesListUpdatedNotification;
import com.resource.management.services.model.LastUpdatedTimestamp;
import com.resource.management.services.model.LastUpdatedTimestampRepository;
import com.resource.management.services.model.Service;
import com.resource.management.services.model.ServiceRepository;

@Controller
public class GetServicesListController
{
   @Autowired
   private ServiceRepository repository;

   @Autowired
   private LastUpdatedTimestampRepository timestampRepository;


   @SubscribeMapping("/services")
   public ServicesListUpdatedNotification handle()
   {
      final List<Service> services = this.repository.findAll();

      final LastUpdatedTimestamp timestampToday = this.timestampRepository.getTodaysTimestamp();
      final LastUpdatedTimestamp timestampTomorrow = this.timestampRepository.getTomorrowTimestamp();

      return new ServicesListUpdatedNotification( services,
            timestampToday == null ? Instant.now().toString() : timestampToday.getTimeStamp(),
            timestampTomorrow == null ? Instant.now().toString() : timestampTomorrow.getTimeStamp() );
   }
}
