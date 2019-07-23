package com.resource.management.services.controller;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.resource.management.api.services.ServicesListUpdatedNotification;
import com.resource.management.api.services.UpdateServiceRequest;
import com.resource.management.services.model.Service;
import com.resource.management.services.model.ServiceMapper;
import com.resource.management.services.model.ServiceRepository;

@Controller
public class UpdateServiceController
{
   @Autowired
   private ServiceRepository repository;

   @Autowired
   private LastUpdatedTimestampService lastUpdatedTimestampService;


   @MessageMapping("/updateService")
   @SendTo("/topic/services")
   public ServicesListUpdatedNotification handle( final UpdateServiceRequest request )
   {
      final String lastUpdate = Instant.now().toString();

      ServicesListUpdatedNotification notification = null;
      if ( request.getService() != null )
      {
         final Optional<Service> serviceOptional = this.repository.findById( request.getService().getId() );
         serviceOptional.ifPresent( service ->
         {
            final Service updatedService = ServiceMapper.toInternal( request.getService(), lastUpdate );
            updatedService.setLastUpdate( lastUpdate );
            this.repository.save( updatedService );
         } );
         notification = this.lastUpdatedTimestampService.getLastUpdatedNotification( request.getService().getDay() );
      }
      else
      {
         notification = this.lastUpdatedTimestampService.getLastUpdatedNotification();
      }

      return notification;
   }
}
