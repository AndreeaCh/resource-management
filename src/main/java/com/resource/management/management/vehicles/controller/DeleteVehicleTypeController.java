package com.resource.management.management.vehicles.controller;

import com.resource.management.api.management.trucks.DeleteVehicleTypeRequest;
import com.resource.management.api.management.trucks.VehicleTypesUpdatedNotification;
import com.resource.management.management.vehicles.model.VehicleRepository;
import com.resource.management.management.vehicles.model.VehicleType;
import com.resource.management.management.vehicles.model.VehicleTypes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import static com.resource.management.management.vehicles.model.VehicleTypes.ID;

@Controller
public class DeleteVehicleTypeController {
    @Autowired
    private VehicleRepository repository;

    @MessageMapping("/deleteVehicleType")
    @SendTo("/topic/vehicleTypes")
    public VehicleTypesUpdatedNotification handle(final DeleteVehicleTypeRequest request) {
        AtomicReference<VehicleTypesUpdatedNotification> vehicleTypesUpdatedNotification = new AtomicReference<>(
                new VehicleTypesUpdatedNotification(new ArrayList<>()));
        Optional<VehicleTypes> vehicleTypesOptional = repository.findById(ID);
        vehicleTypesOptional.ifPresent(vehicleTypes -> {
            List<VehicleType> vehicleTypeList = vehicleTypes.getVehicleTypes();
            Optional<VehicleType> vehicleTypeOptional = vehicleTypeList.stream().filter(vehicleType -> vehicleType.getId().equals(request.getId())).findFirst();
            vehicleTypeOptional.ifPresent(vehicleTypeList::remove);
            vehicleTypeList.sort(getVehicleTypesComparator());
            repository.save(vehicleTypes);
            vehicleTypesUpdatedNotification.set(new VehicleTypesUpdatedNotification(vehicleTypeList));
        });

        return vehicleTypesUpdatedNotification.get();
    }

    private Comparator<? super VehicleType> getVehicleTypesComparator() {
        return (Comparator<VehicleType>) (t1, t2) -> t1.getShortName().compareToIgnoreCase(t2.getShortName());
    }
}
