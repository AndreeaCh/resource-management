package com.resource.management.services.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.resource.management.api.services.DeleteServiceRequest;
import com.resource.management.api.services.ServicesListUpdatedNotification;
import com.resource.management.services.model.LastUpdatedTimestamp;
import com.resource.management.services.model.LastUpdatedTimestampRepository;
import com.resource.management.services.model.Service;
import com.resource.management.services.model.ServiceRepository;

@Controller
public class DeleteServiceController {
    @Autowired
    private ServiceRepository repository;

    @Autowired
    private LastUpdatedTimestampRepository timestampRepository;

    @MessageMapping("/deleteService")
    @SendTo("/topic/services")
    public ServicesListUpdatedNotification handle(final DeleteServiceRequest request) {
        repository.deleteById(request.getId());
        final LastUpdatedTimestamp lastUpdatedTimestamp = new LastUpdatedTimestamp( "timeStamp",
              Instant.now().toString() );
        timestampRepository.save( lastUpdatedTimestamp );
        List<Service> services = repository.findAll();
        return new ServicesListUpdatedNotification( services, lastUpdatedTimestamp.getTimeStamp() );
    }
}
