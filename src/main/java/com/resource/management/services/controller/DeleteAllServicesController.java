package com.resource.management.services.controller;

import com.resource.management.api.services.DeleteAllServicesRequest;
import com.resource.management.api.services.ServicesListUpdatedNotification;
import com.resource.management.services.model.Service;
import com.resource.management.services.model.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Controller
public class DeleteAllServicesController {
    @Autowired
    private ServiceRepository repository;

    @MessageMapping("/deleteAllServices")
    @SendTo("/topic/services")
    public ServicesListUpdatedNotification handle(final DeleteAllServicesRequest request) {
        repository.deleteAll();
        List<Service> services = repository.findAll();
        Instant lastUpdate = services.stream().map(s -> Instant.parse(s.getLastUpdate())).max(Comparator.naturalOrder()).orElse(null);
        return new ServicesListUpdatedNotification(services, lastUpdate != null ? lastUpdate.toString() : null);
    }
}
