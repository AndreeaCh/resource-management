package com.resource.management.resource.service;

import lombok.*;

import java.util.Map;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SubUnitReport {
    private String subUnitName;
    private Map<String, Report> interventionTechniqueResourcesReport;
    private Map<String, Report> equipmentReport;
    private Report interventionTechniqueTotal;
    private Report allResourcesTotal;
    private Report equipmentTotal;
}
