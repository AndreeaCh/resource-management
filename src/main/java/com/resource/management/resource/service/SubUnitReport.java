package com.resource.management.resource.service;

import lombok.*;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SubUnitReport {
    private String subUnitName;
    private List<VehicleTypeReport> firstInterventionResourceReport;
    private List<VehicleTypeReport> otherResourceReport;
    private List<EquipmentReport> equipmentReport;
    private Report firstInterventionTotal;
    private Report otherResourcesTotal;
    private Report allResourcesTotal;
    private Report equipmentTotal;
}
