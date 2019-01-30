package com.resource.management.services.controller;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.resource.management.api.services.AddServiceRequest;
import com.resource.management.api.services.ServicesListUpdatedNotification;
import com.resource.management.services.model.LastUpdatedTimestamp;
import com.resource.management.services.model.LastUpdatedTimestampRepository;
import com.resource.management.services.model.Service;
import com.resource.management.services.model.ServiceRepository;

@Controller
public class AddServiceController {
    @Autowired
    private ServiceRepository repository;

    @Autowired
    private LastUpdatedTimestampRepository timestampRepository;

    @MessageMapping("/addService")
    @SendTo("/topic/services")
    public ServicesListUpdatedNotification handle(final AddServiceRequest request) {
        Service service = new Service(UUID.randomUUID().toString(),
                request.getName(),
                request.getTitle(),
                request.getRole(),
                request.getContact(),
                Instant.now().toString());
        repository.save(service);

        final LastUpdatedTimestamp lastUpdatedTimestamp = new LastUpdatedTimestamp( "timeStamp",
              Instant.now().toString() );
        timestampRepository.save( lastUpdatedTimestamp );

        return new ServicesListUpdatedNotification( repository.findAll(), lastUpdatedTimestamp.getTimeStamp() );
    }
}
