package com.resource.management.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import com.resource.management.api.SubscribeResourceLogResponse;
import com.resource.management.data.ResourceLog;
import com.resource.management.data.ResourceLogRepository;

@Controller
public class ResourceLogController
{
   private static final Logger LOG = LoggerFactory.getLogger( ResourceLogController.class );

   @Autowired
   private ResourceLogRepository repository;


   @SubscribeMapping("/resourcelogs/{plateNumber}")
   public SubscribeResourceLogResponse handleResourceLogMessage( @DestinationVariable final String plateNumber )
   {
      final List<ResourceLog> resourceLogs = this.repository.findByPlateNumber( plateNumber );
      return new SubscribeResourceLogResponse( resourceLogs );
   }
}
