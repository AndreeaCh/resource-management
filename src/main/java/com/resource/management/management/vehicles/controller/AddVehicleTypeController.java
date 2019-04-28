package com.resource.management.management.vehicles.controller;

import com.resource.management.api.management.trucks.AddVehicleTypeRequest;
import com.resource.management.api.management.trucks.VehicleTypesUpdatedNotification;
import com.resource.management.management.vehicles.model.VehicleType;
import com.resource.management.management.vehicles.model.VehicleRepository;
import com.resource.management.management.vehicles.model.VehicleTypes;
import java.util.ArrayList;
import java.util.Collections;
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
public class AddVehicleTypeController {
    @Autowired
    private VehicleRepository repository;

    @MessageMapping("/addVehicleType")
    @SendTo("/topic/vehicleTypes")
    public VehicleTypesUpdatedNotification handle(final AddVehicleTypeRequest request) {
        AtomicReference<VehicleTypesUpdatedNotification> vehicleTypesUpdatedNotification
                = new AtomicReference<>(new VehicleTypesUpdatedNotification(new ArrayList<>()));
        VehicleType vehicleType = new VehicleType(request.getId(), request.getShortName(), request.getLongName());
        Optional<VehicleTypes> vehicleTypesOptional = repository.findById(ID);
        if (vehicleTypesOptional.isPresent()) {
            VehicleTypes vehicleTypes = vehicleTypesOptional.get();
            List<VehicleType> vehicleTypeList = vehicleTypes.getVehicleTypes();
            vehicleTypeList.add(vehicleType);
            vehicleTypeList.sort(getVehiclesComparator());
            repository.save(vehicleTypes);
            vehicleTypesUpdatedNotification.set(new VehicleTypesUpdatedNotification(vehicleTypeList));
        } else {
            List<VehicleType> vehicleTypeList = Collections.singletonList(vehicleType);
            VehicleTypes vehicleTypes = new VehicleTypes(ID, vehicleTypeList);
            repository.save(vehicleTypes);
            vehicleTypesUpdatedNotification.set(new VehicleTypesUpdatedNotification(vehicleTypeList));
        }

        return vehicleTypesUpdatedNotification.get();
    }

    private Comparator<? super VehicleType> getVehiclesComparator() {
        return (Comparator<VehicleType>) (t1, t2) -> t1.getShortName().compareToIgnoreCase(t2.getShortName());
    }
}
