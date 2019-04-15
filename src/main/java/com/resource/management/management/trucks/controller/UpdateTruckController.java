package com.resource.management.management.trucks.controller;

import static com.resource.management.management.trucks.model.Trucks.ID;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.resource.management.api.management.trucks.TrucksListUpdatedNotification;
import com.resource.management.api.management.trucks.UpdateTruckRequest;
import com.resource.management.management.trucks.model.Truck;
import com.resource.management.management.trucks.model.TruckRepository;
import com.resource.management.management.trucks.model.Trucks;
import com.resource.management.resource.model.SubUnit;
import com.resource.management.resource.model.SubUnitMapper;
import com.resource.management.resource.service.NotificationService;
import com.resource.management.resource.service.SubUnitsService;

@Controller
public class UpdateTruckController {
    @Autowired
    private TruckRepository repository;

    @Autowired
    private SubUnitsService subUnitsService;

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/updateTruck")
    @SendTo("/topic/trucks")
    public TrucksListUpdatedNotification handle(final UpdateTruckRequest request) {
        AtomicReference<TrucksListUpdatedNotification> trucksListUpdatedNotification = new AtomicReference<>(new TrucksListUpdatedNotification(new ArrayList<>()));
        Optional<Trucks> trucksOptional = repository.findById(ID);
        trucksOptional.ifPresent(trucks -> {
            List<Truck> truckList = trucks.getTrucks();
            Optional<Truck> truckOptional = truckList.stream().filter(truck -> truck.getId().equals(request.getId())).findFirst();
            truckOptional.ifPresent(truck -> {
                Truck updatedTruck = new Truck(request.getId(), request.getShortName(), request.getLongName());
                String truckOldShortName = truck.getShortName();
                truckList.remove(truck);
                truckList.add(updatedTruck);
                updateResourcesWithVehicleType( request, truckOldShortName );
            });

            repository.save(trucks);

            truckList.sort(getTrucksComparator());
            trucksListUpdatedNotification.set(new TrucksListUpdatedNotification(truckList));
        });

        return trucksListUpdatedNotification.get();
    }


    private void updateResourcesWithVehicleType( final UpdateTruckRequest request, final String truckOldShortName )
    {
        final List<SubUnit> subUnits = subUnitsService.updateAllResourcesWithVehicleType(
              request.getShortName(), truckOldShortName );
        subUnits.forEach( subUnit -> notificationService.publishSubUnitNotification( SubUnitMapper.toApi( subUnit ) ) );
    }


    private Comparator<? super Truck> getTrucksComparator() {
        return (Comparator<Truck>) (t1, t2) -> t1.getShortName().compareToIgnoreCase(t2.getShortName());
    }
}
