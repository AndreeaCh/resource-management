package com.resource.management.services.controller;

import com.resource.management.api.services.DeleteAllServicesRequest;
import com.resource.management.api.services.ServicesListUpdatedNotification;
import com.resource.management.services.model.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.ArrayList;

@Controller
public class DeleteAllServicesController {
    @Autowired
    private ServiceRepository repository;

    @MessageMapping("/deleteAllServices")
    @SendTo("/topic/services")
    public ServicesListUpdatedNotification handle(final DeleteAllServicesRequest request) {
        repository.deleteAll();
        return new ServicesListUpdatedNotification(new ArrayList<>(), Instant.now().toString());
    }
}
