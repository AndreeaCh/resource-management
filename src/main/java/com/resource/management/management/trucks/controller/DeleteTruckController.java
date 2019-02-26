package com.resource.management.management.trucks.controller;

import com.resource.management.api.management.trucks.DeleteTruckRequest;
import com.resource.management.api.management.trucks.TrucksListUpdatedNotification;
import com.resource.management.management.trucks.model.TruckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class DeleteTruckController {
    @Autowired
    private TruckRepository repository;

    @MessageMapping("/deleteTruck")
    @SendTo("/topic/trucks")
    public TrucksListUpdatedNotification handle(final DeleteTruckRequest request) {
        repository.deleteById(request.getId());
        return new TrucksListUpdatedNotification(repository.findAll());
    }
}
