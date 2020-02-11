package com.resource.management.resource.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.resource.management.api.management.subunits.DeleteSubUnitRequest;
import com.resource.management.api.management.subunits.DeleteSubUnitResponse;
import com.resource.management.api.resources.StatusCode;
import com.resource.management.resource.model.SubUnit;
import com.resource.management.resource.model.SubUnitsRepository;
import com.resource.management.resource.service.NotificationService;
import com.resource.management.resource.service.SubUnitsService;
import com.resource.management.services.model.ServiceRepository;

@Controller
public class DeleteSubUnitController
{
   @Autowired
   private SubUnitsService service;

   @Autowired
   private ServiceRepository serviceRepository;

   @Autowired
   private SubUnitsRepository subUnitRepository;

   @Autowired
   private NotificationService notificationService;


   @MessageMapping("/deleteSubUnit")
   @SendTo("/topic/unitDeletedNotification")
   public DeleteSubUnitResponse handle( final DeleteSubUnitRequest request,
         final SimpMessageHeaderAccessor headerAccessor )
   {
      final Optional<SubUnit> subUnit = this.subUnitRepository.findById( request.getId() );
      if ( subUnit.isPresent() )
      {
         final String subUnitName = subUnit.get().getName();
         this.serviceRepository.deleteBySubUnit( subUnitName );
      }
      final String ipAddress = headerAccessor.getSessionAttributes().get( "ip" ).toString();
      this.service.deleteSubUnit( request.getId(), ipAddress );
      this.notificationService.publishInitialSubUnitsNotification();
      return new DeleteSubUnitResponse( StatusCode.OK );
   }
}
