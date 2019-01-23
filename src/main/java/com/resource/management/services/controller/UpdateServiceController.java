package com.resource.management.services.controller;

import com.resource.management.api.services.ServicesListUpdatedNotification;
import com.resource.management.api.services.UpdateServiceRequest;
import com.resource.management.resource.model.ServiceMapper;
import com.resource.management.services.model.Service;
import com.resource.management.services.model.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.Optional;

@Controller
public class UpdateServiceController {
    @Autowired
    private ServiceRepository repository;

    @MessageMapping("/updateService")
    @SendTo("/topic/services")
    public ServicesListUpdatedNotification handle(final UpdateServiceRequest request) {
        if (request.getService() != null) {
            Optional<Service> serviceOptional = repository.findById(request.getService().getId());
            serviceOptional.ifPresent(service -> {
                Service updatedService = ServiceMapper.toInternal(request.getService());
                updatedService.setLastUpdate(Instant.now().toString());
                repository.save(updatedService);
            });
        }

        return new ServicesListUpdatedNotification(repository.findAll(), Instant.now().toString());
    }
}
