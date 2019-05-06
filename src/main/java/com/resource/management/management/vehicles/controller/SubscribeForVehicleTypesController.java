package com.resource.management.management.vehicles.controller;

import com.resource.management.api.management.trucks.VehicleTypesUpdatedNotification;
import com.resource.management.management.vehicles.model.VehicleType;
import com.resource.management.management.vehicles.model.VehicleRepository;
import com.resource.management.management.vehicles.model.VehicleTypes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import static com.resource.management.management.vehicles.model.VehicleTypes.ID;

@Controller
public class SubscribeForVehicleTypesController {
    @Autowired
    private VehicleRepository repository;

    @SubscribeMapping("/vehicleTypes")
    public VehicleTypesUpdatedNotification handle() {
        AtomicReference<VehicleTypesUpdatedNotification> vehicleTypesUpdatedNotification = new AtomicReference<>(
                new VehicleTypesUpdatedNotification(new ArrayList<>()));
        Optional<VehicleTypes> vehiclesOptional = repository.findById(ID);
        vehiclesOptional.ifPresent(vehicleTypes -> {
            List<VehicleType> vehicleTypeList = vehicleTypes.getVehicleTypes();
            vehicleTypeList.sort(getVehicleTypesComparator());
            vehicleTypesUpdatedNotification.set(new VehicleTypesUpdatedNotification(vehicleTypeList));
        });

        return vehicleTypesUpdatedNotification.get();
    }

    private Comparator<? super VehicleType> getVehicleTypesComparator() {
        return (Comparator<VehicleType>) (t1, t2) -> t1.getShortName().compareToIgnoreCase(t2.getShortName());
    }
}
