package com.resource.management.management.trucks.controller;

import com.resource.management.api.management.trucks.AddTruckRequest;
import com.resource.management.api.management.trucks.TrucksListUpdatedNotification;
import com.resource.management.management.trucks.model.Truck;
import com.resource.management.management.trucks.model.TruckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class AddTruckController {
    @Autowired
    private TruckRepository repository;

    @MessageMapping("/addTruck")
    @SendTo("/topic/trucks")
    public TrucksListUpdatedNotification handle(final AddTruckRequest request) {
        Truck function = new Truck(UUID.randomUUID().toString(), request.getShortName(), request.getLongName());
        repository.save(function);
        return new TrucksListUpdatedNotification(repository.findAll());
    }
}
