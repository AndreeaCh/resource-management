package com.resource.management.services.controller;

import com.resource.management.api.services.DeleteServiceRequest;
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
public class DeleteServiceController {
    @Autowired
    private ServiceRepository repository;

    @MessageMapping("/deleteService")
    @SendTo("/topic/services")
    public ServicesListUpdatedNotification handle(final DeleteServiceRequest request) {
        repository.deleteById(request.getId());
        List<Service> services = repository.findAll();
        Instant lastUpdate = services.stream().map(s -> Instant.parse(s.getLastUpdate())).max(Comparator.naturalOrder()).orElse(null);
        return new ServicesListUpdatedNotification(services, lastUpdate != null ? lastUpdate.toString() : null);
    }
}
