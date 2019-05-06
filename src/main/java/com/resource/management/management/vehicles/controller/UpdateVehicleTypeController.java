package com.resource.management.management.vehicles.controller;

import com.resource.management.api.management.trucks.UpdateVehicleTypeRequest;
import com.resource.management.api.management.trucks.VehicleTypesUpdatedNotification;
import com.resource.management.management.vehicles.model.VehicleRepository;
import com.resource.management.management.vehicles.model.VehicleType;
import com.resource.management.management.vehicles.model.VehicleTypes;
import com.resource.management.resource.model.SubUnit;
import com.resource.management.resource.model.SubUnitMapper;
import com.resource.management.resource.service.NotificationService;
import com.resource.management.resource.service.SubUnitsService;
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
public class UpdateVehicleTypeController {
    @Autowired
    private VehicleRepository repository;

    @Autowired
    private SubUnitsService subUnitsService;

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/updateVehicleType")
    @SendTo("/topic/vehicleTypes")
    public VehicleTypesUpdatedNotification handle(final UpdateVehicleTypeRequest request) {
        AtomicReference<VehicleTypesUpdatedNotification> vehicleTypesUpdatedNotification = new AtomicReference<>(
                new VehicleTypesUpdatedNotification(new ArrayList<>()));
        Optional<VehicleTypes> vehiclesOptional = repository.findById(ID);
        vehiclesOptional.ifPresent(vehicleType -> {
            List<VehicleType> vehicleTypeList = vehicleType.getVehicleTypes();
            Optional<VehicleType> vehicleTypeOptional = vehicleTypeList.stream().filter(type -> type.getId().equals(request.getId())).findFirst();
            vehicleTypeOptional.ifPresent(type -> {
                VehicleType updatedVehicleType = new VehicleType(request.getId(), request.getShortName(), request.getLongName());
                String oldShortName = type.getShortName();
                vehicleTypeList.remove(type);
                vehicleTypeList.add(updatedVehicleType);
                updateResourcesWithVehicleType(request, oldShortName);
            });

            repository.save(vehicleType);

            vehicleTypeList.sort(getVehicleTypesComparator());
            vehicleTypesUpdatedNotification.set(new VehicleTypesUpdatedNotification(vehicleTypeList));
        });

        return vehicleTypesUpdatedNotification.get();
    }


    private void updateResourcesWithVehicleType(final UpdateVehicleTypeRequest request, final String oldShortName) {
        final List<SubUnit> subUnits = subUnitsService.updateAllResourcesWithVehicleType(request.getShortName(), oldShortName);
        subUnits.forEach(subUnit -> notificationService.publishSubUnitNotification(SubUnitMapper.toApi(subUnit)));
    }


    private Comparator<? super VehicleType> getVehicleTypesComparator() {
        return (Comparator<VehicleType>) (t1, t2) -> t1.getShortName().compareToIgnoreCase(t2.getShortName());
    }
}
