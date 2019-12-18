package com.resource.management.resource.service;

import com.resource.management.management.vehicles.model.VehicleType;
import com.resource.management.resource.model.Equipment;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SubUnitReport {
    private String subUnitName;
    private List<VehicleTypeReport> firstInterventionResourceReport;
    private Map<VehicleType, Report> otherResourceReport;
    private Map<Equipment, Report> equipmentReport;
    private Report firstInterventionTotal;
    private Report otherResourcesTotal;
    private Report allResourcesTotal;
    private Report equipmentTotal;
}
