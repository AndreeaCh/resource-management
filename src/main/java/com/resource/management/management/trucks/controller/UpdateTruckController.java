package com.resource.management.management.trucks.controller;

import com.resource.management.api.management.functions.FunctionsListUpdatedNotification;
import com.resource.management.api.management.functions.UpdateFunctionRequest;
import com.resource.management.api.management.trucks.TrucksListUpdatedNotification;
import com.resource.management.api.management.trucks.UpdateTruckRequest;
import com.resource.management.management.trucks.model.Truck;
import com.resource.management.management.trucks.model.TruckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class UpdateTruckController {
    @Autowired
    private TruckRepository repository;

    @MessageMapping("/updateTruck")
    @SendTo("/topic/trucks")
    public TrucksListUpdatedNotification handle(final UpdateTruckRequest request) {
        if (request.getId() != null) {
            Optional<Truck> functionOptional = repository.findById(request.getId());
            functionOptional.ifPresent(service -> {
                Truck updatedTruck = new Truck(request.getId(), request.getShortName(), request.getLongName());
                repository.save(updatedTruck);
            });
        }

        return new TrucksListUpdatedNotification(repository.findAll());
    }
}
