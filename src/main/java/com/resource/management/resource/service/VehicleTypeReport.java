package com.resource.management.resource.service;

import com.resource.management.management.vehicles.model.VehicleType;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class VehicleTypeReport {
    private VehicleType vehicleType;
    private Report report;
}
