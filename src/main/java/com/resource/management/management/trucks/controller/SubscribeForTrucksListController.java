package com.resource.management.management.trucks.controller;

import com.resource.management.api.management.trucks.TrucksListUpdatedNotification;
import com.resource.management.management.trucks.model.Truck;
import com.resource.management.management.trucks.model.TruckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class SubscribeForTrucksListController {
    @Autowired
    private TruckRepository repository;

    @SubscribeMapping("/trucks")
    public TrucksListUpdatedNotification handle() {
        List<Truck> trucks = repository.findAll();
        return new TrucksListUpdatedNotification(trucks);
    }
}
