package com.resource.management.management.trucks.controller;

import com.resource.management.api.management.trucks.AddTruckRequest;
import com.resource.management.api.management.trucks.TrucksListUpdatedNotification;
import com.resource.management.management.trucks.model.Truck;
import com.resource.management.management.trucks.model.TruckRepository;
import com.resource.management.management.trucks.model.Trucks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.resource.management.management.trucks.model.Trucks.ID;

@Controller
public class AddTruckController {
    @Autowired
    private TruckRepository repository;

    @MessageMapping("/addTruck")
    @SendTo("/topic/trucks")
    public TrucksListUpdatedNotification handle(final AddTruckRequest request) {
        AtomicReference<TrucksListUpdatedNotification> trucksListUpdatedNotification = new AtomicReference<>(new TrucksListUpdatedNotification(new ArrayList<>()));
        Truck truck = new Truck(UUID.randomUUID().toString(), request.getShortName(), request.getLongName());
        Optional<Trucks> trucksOptional = repository.findById(ID);
        trucksOptional.ifPresent(trucks ->
        {
            List<Truck> truckList = trucks.getTrucks();
            truckList.add(truck);
            truckList.sort(getTrucksComparator());
            repository.save(trucks);
            trucksListUpdatedNotification.set(new TrucksListUpdatedNotification(truckList));
        });

        return trucksListUpdatedNotification.get();
    }

    private Comparator<? super Truck> getTrucksComparator() {
        return (Comparator<Truck>) (t1, t2) -> t1.getShortName().compareToIgnoreCase(t2.getShortName());
    }
}
