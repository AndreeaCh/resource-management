package com.resource.management.services.controller;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.resource.management.api.services.ServicesListUpdatedNotification;
import com.resource.management.api.services.UpdateServiceRequest;
import com.resource.management.resource.model.ServiceMapper;
import com.resource.management.services.model.LastUpdatedTimestamp;
import com.resource.management.services.model.LastUpdatedTimestampRepository;
import com.resource.management.services.model.Service;
import com.resource.management.services.model.ServiceRepository;

@Controller
public class UpdateServiceController {
    @Autowired
    private ServiceRepository repository;

    @Autowired
    private LastUpdatedTimestampRepository timestampRepository;

    @MessageMapping("/updateService")
    @SendTo("/topic/services")
    public ServicesListUpdatedNotification handle(final UpdateServiceRequest request) {
        String lastUpdate = Instant.now().toString();
        final LastUpdatedTimestamp lastUpdatedTimestamp = new LastUpdatedTimestamp( "timeStamp",
              lastUpdate );
        if (request.getService() != null) {
            Optional<Service> serviceOptional = repository.findById(request.getService().getId());
            serviceOptional.ifPresent(service -> {
                Service updatedService = ServiceMapper.toInternal(request.getService(), lastUpdate);
                updatedService.setLastUpdate(lastUpdate);
                repository.save(updatedService);
                timestampRepository.save( lastUpdatedTimestamp );
            });
        }

        return new ServicesListUpdatedNotification( repository.findAll(), lastUpdatedTimestamp.getTimeStamp() );
    }
}
