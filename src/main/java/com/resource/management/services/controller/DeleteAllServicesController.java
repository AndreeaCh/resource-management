package com.resource.management.services.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.resource.management.api.services.DeleteAllServicesRequest;
import com.resource.management.api.services.ServicesListUpdatedNotification;
import com.resource.management.services.model.ServiceRepository;

@Controller
public class DeleteAllServicesController
{
   @Autowired
   private ServiceRepository repository;

   @Autowired
   private LastUpdatedTimestampService lastUpdatedTimestampService;


   @MessageMapping("/deleteAllServices")
   @SendTo("/topic/services")
   public ServicesListUpdatedNotification handle( final DeleteAllServicesRequest request )
   {
      if ( request.getServicesDay() != null )
      {
         this.repository.deleteByDay( request.getServicesDay() );

         return this.lastUpdatedTimestampService.getLastUpdatedNotification( request.getServicesDay() );
      }

      return this.lastUpdatedTimestampService.getLastUpdatedNotification();
   }
}
