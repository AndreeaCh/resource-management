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
    private Map<String, Report> firstInterventionResourceReport;
    private Map<String, Report> otherResourceReport;
    private Map<String, Report> equipmentReport;
    private Report firstInterventionTotal;
    private Report otherResourcesTotal;
    private Report allResourcesTotal;
    private Report equipmentTotal;
}
