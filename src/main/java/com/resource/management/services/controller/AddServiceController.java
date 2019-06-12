package com.resource.management.services.controller;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.resource.management.api.services.AddServiceRequest;
import com.resource.management.api.services.ServicesListUpdatedNotification;
import com.resource.management.services.model.Service;
import com.resource.management.services.model.ServiceRepository;

@Controller
public class AddServiceController
{
   @Autowired
   private ServiceRepository repository;

   @Autowired
   private LastUpdatedTimestampService lastUpdatedTimestampService;


   @MessageMapping("/addService")
   @SendTo("/topic/services")
   public ServicesListUpdatedNotification handle( final AddServiceRequest request )
   {
      final Service service =
            new Service( UUID.randomUUID().toString(), request.getName(), request.getTitle(), request.getRole(),
                  request.getContact(), request.getDay(), Instant.now().toString() );
      this.repository.save( service );

      return this.lastUpdatedTimestampService.getLastUpdatedNotification( request.getDay() );
   }
}
