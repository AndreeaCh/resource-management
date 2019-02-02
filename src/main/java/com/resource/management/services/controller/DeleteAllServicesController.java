package com.resource.management.services.controller;

import java.time.Instant;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.resource.management.api.services.DeleteAllServicesRequest;
import com.resource.management.api.services.ServicesListUpdatedNotification;
import com.resource.management.services.model.LastUpdatedTimestamp;
import com.resource.management.services.model.LastUpdatedTimestampRepository;
import com.resource.management.services.model.ServiceRepository;

@Controller
public class DeleteAllServicesController {
    @Autowired
    private ServiceRepository repository;

    @Autowired
    private LastUpdatedTimestampRepository timestampRepository;

    @MessageMapping("/deleteAllServices")
    @SendTo("/topic/services")
    public ServicesListUpdatedNotification handle(final DeleteAllServicesRequest request) {
        repository.deleteAll();
        final LastUpdatedTimestamp lastUpdatedTimestamp = new LastUpdatedTimestamp( "timeStamp",
              Instant.now().toString() );
        timestampRepository.save( lastUpdatedTimestamp );
        return new ServicesListUpdatedNotification( new ArrayList<>(), lastUpdatedTimestamp.getTimeStamp() );
    }
}
