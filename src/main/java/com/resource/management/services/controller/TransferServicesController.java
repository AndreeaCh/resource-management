package com.resource.management.services.controller;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.resource.management.api.services.ServicesListUpdatedNotification;
import com.resource.management.api.services.TransferServicesRequest;
import com.resource.management.services.model.LastUpdatedTimestamp;
import com.resource.management.services.model.LastUpdatedTimestampRepository;
import com.resource.management.services.model.Service;
import com.resource.management.services.model.ServiceRepository;

@Controller
public class TransferServicesController
{
   private static final String TOMORROW = "TOMORROW";

   private static final String TODAY = "TODAY";

   @Autowired
   private ServiceRepository repository;

   @Autowired
   private LastUpdatedTimestampRepository timestampRepository;


   @MessageMapping("/transferServices")
   @SendTo("/topic/services")
   public ServicesListUpdatedNotification handle( final TransferServicesRequest request )
   {
      this.repository.deleteByDay( TODAY );
      final Iterable<Service> servicesTomorrow = this.repository.findByDay( TOMORROW );
      servicesTomorrow.forEach( service ->
      {
         final Service transferredService = service.serviceWithDay( TODAY );
         this.repository.save( transferredService );
      } );

      final LastUpdatedTimestamp timestampToday =
            new LastUpdatedTimestamp( "timeStampToday", Instant.now().toString() );
      final LastUpdatedTimestamp timestampTomorrow =
            this.timestampRepository.findById( "timeStampTomorrow" ).orElse( null );
      this.timestampRepository.save( timestampToday );

      return new ServicesListUpdatedNotification( this.repository.findAll(), timestampToday.getTimeStamp(),
            timestampTomorrow == null ? Instant.now().toString() : timestampTomorrow.getTimeStamp() );
   }
}
