package com.resource.management.services.controller;

import com.resource.management.api.services.DeleteServiceRequest;
import com.resource.management.api.services.ServicesListUpdatedNotification;
import com.resource.management.services.model.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class DeleteServiceController {
    @Autowired
    private ServiceRepository repository;

    @MessageMapping("/deleteService")
    @SendTo("/topic/services")
    public ServicesListUpdatedNotification handle(final DeleteServiceRequest request) {
        repository.deleteById(request.getId());
        return new ServicesListUpdatedNotification(repository.findAll());
    }
}
