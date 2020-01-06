package com.resource.management.resource.service;

import com.resource.management.management.vehicles.model.VehicleType;
import com.resource.management.resource.model.Equipment;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EquipmentReport {
    private Equipment equipment;
    private Report report;
}
