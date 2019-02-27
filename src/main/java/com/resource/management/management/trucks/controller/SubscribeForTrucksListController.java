package com.resource.management.management.trucks.controller;

import com.resource.management.api.management.trucks.TrucksListUpdatedNotification;
import com.resource.management.management.trucks.model.Truck;
import com.resource.management.management.trucks.model.TruckRepository;
import com.resource.management.management.trucks.model.Trucks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static com.resource.management.management.trucks.model.Trucks.ID;

@Controller
public class SubscribeForTrucksListController {
    @Autowired
    private TruckRepository repository;

    @SubscribeMapping("/trucks")
    public TrucksListUpdatedNotification handle() {
        AtomicReference<TrucksListUpdatedNotification> trucksListUpdatedNotification = new AtomicReference<>(new TrucksListUpdatedNotification(new ArrayList<>()));
        Optional<Trucks> trucksOptional = repository.findById(ID);
        trucksOptional.ifPresent(trucks -> {
            List<Truck> truckList = trucks.getTrucks();
            truckList.sort(getTrucksComparator());
            trucksListUpdatedNotification.set(new TrucksListUpdatedNotification(truckList));
        });

        return trucksListUpdatedNotification.get();
    }

    private Comparator<? super Truck> getTrucksComparator() {
        return (Comparator<Truck>) (t1, t2) -> t1.getShortName().compareToIgnoreCase(t2.getShortName());
    }
}
