package com.resource.management.services.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.resource.management.api.services.DeleteServiceRequest;
import com.resource.management.api.services.ServicesListUpdatedNotification;
import com.resource.management.services.model.Service;
import com.resource.management.services.model.ServiceRepository;

@Controller
public class DeleteServiceController
{
   @Autowired
   private ServiceRepository repository;

   @Autowired
   private LastUpdatedTimestampService lastUpdatedTimestampService;


   @MessageMapping("/deleteService")
   @SendTo("/topic/services")
   public ServicesListUpdatedNotification handle( final DeleteServiceRequest request )
   {
      final Optional<Service> service = this.repository.findById( request.getId() );
      String day = null;
      if ( service.isPresent() )
      {
         day = service.get().getDay();
      }

      this.repository.deleteById( request.getId() );

      return this.lastUpdatedTimestampService.getLastUpdatedNotification( day );
   }
}
