package com.resource.management.api.management.trucks;

import com.resource.management.management.vehicles.model.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VehicleTypesUpdatedNotification {
    private List<VehicleType> vehicleTypes;
}
