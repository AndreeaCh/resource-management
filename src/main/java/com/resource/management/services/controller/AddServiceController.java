package com.resource.management.services.controller;

import com.resource.management.api.services.AddServiceRequest;
import com.resource.management.api.services.ServicesListUpdatedNotification;
import com.resource.management.services.model.Service;
import com.resource.management.services.model.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.UUID;

@Controller
public class AddServiceController {
    @Autowired
    private ServiceRepository repository;

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
        return new ServicesListUpdatedNotification(repository.findAll(), service.getLastUpdate());
    }
}
