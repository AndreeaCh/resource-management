package com.resource.management.resource.service;

import com.resource.management.management.vehicles.model.VehicleType;
import com.resource.management.resource.model.Equipment;
import com.resource.management.resource.model.Resource;
import com.resource.management.resource.model.ResourceStatus;
import com.resource.management.resource.model.ResourceType;
import com.resource.management.resource.model.SubUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ReportBuilder {
    public AllSubUnitsReport buildReport(final List<SubUnit> subUnits, final List<VehicleType> vehicleTypeList) {
        AllSubUnitsReport report = new AllSubUnitsReport();
        HashMap<String, SubUnitReport> subUnitReports = new HashMap<>();
        report.setSubUnitReports(subUnitReports);

        subUnits.forEach(subUnit -> {
            SubUnitReport subUnitReport = new SubUnitReport();
            subUnitReport.setSubUnitName(subUnit.getName());
            List<VehicleType> firstInterventionResourcesTypes = getFirstInterventionResourcesTypes(subUnits, vehicleTypeList);
            Map<String, Report> fiResourceMap = new HashMap<>();
            firstInterventionResourcesTypes.forEach(resourceType -> {
                List<Resource> resourcesOfType = getResourcesOfType(subUnit, resourceType.getShortName());
                fiResourceMap.put(resourceType.getLongName(), buildReportForResource(resourcesOfType));
            });
            subUnitReport.setFirstInterventionResourceReport(fiResourceMap);

            Map<String, Report> otherResourceMap = new HashMap<>();
            List<VehicleType> otherResourcesTypes = getOtherResourcesTypes(subUnits, vehicleTypeList);
            otherResourcesTypes.forEach(resourceType -> {
                List<Resource> resourcesOfType = getResourcesOfType(subUnit, resourceType.getShortName());
                otherResourceMap.put(resourceType.getLongName(), buildReportForResource(resourcesOfType));
            });
            subUnitReport.setOtherResourceReport(otherResourceMap);

            Map<String, Report> equipmentResourceMap = new HashMap<>();
            List<String> equipmentResourceTypes = getEquipmentTypes(subUnits);
            equipmentResourceTypes.forEach(resourceType -> {
                List<Equipment> equipmentList = getEquipmentOfType(subUnit, resourceType);
                equipmentResourceMap.put(resourceType, buildReportForEquipment(equipmentList));
            });
            subUnitReport.setEquipmentReport(equipmentResourceMap);

            setFirstInterventionTotal(subUnitReport);
            setOtherResourcesTotal(subUnitReport);
            setAllResourcesTotal(subUnitReport);
            setEquipmentsTotal(subUnitReport);
            subUnitReports.put(subUnit.getName(), subUnitReport);
        });

        computeTotals(report);
        return report;
    }

    private void computeTotals(final AllSubUnitsReport report) {
        SubUnitReport totals = new SubUnitReport();
        report.setTotals(totals);
        Map<String, Report> firstInterventionTotals = report.getSubUnitReports()
                                                            .values()
                                                            .stream()
                                                            .flatMap(r -> r.getFirstInterventionResourceReport().entrySet().stream())
                                                            .collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue(), (v1, v2) -> addReports(v1, v2)));
        totals.setFirstInterventionResourceReport(firstInterventionTotals);

        Report firstInterventionTotal = new Report();
        firstInterventionTotals.values()
                               .forEach(r -> {
                                   firstInterventionTotal.setUsable(firstInterventionTotal.getUsable() + r.getUsable());
                                   firstInterventionTotal.setReserves(firstInterventionTotal.getReserves() + r.getReserves());
                                   firstInterventionTotal.setUnusable(firstInterventionTotal.getUnusable() + r.getUnusable());
                               });
        totals.setFirstInterventionTotal(firstInterventionTotal);

        Map<String, Report> otherResourcesTotals = report.getSubUnitReports()
                                                         .values()
                                                         .stream()
                                                         .flatMap(r -> r.getOtherResourceReport().entrySet().stream())
                                                         .collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue(), (v1, v2) -> addReports(v1, v2)));
        totals.setOtherResourceReport(otherResourcesTotals);
        Report otherResourcesTotal = new Report();
        otherResourcesTotals.values()
                            .forEach(r -> {
                                otherResourcesTotal.setUsable(otherResourcesTotal.getUsable() + r.getUsable());
                                otherResourcesTotal.setReserves(otherResourcesTotal.getReserves() + r.getReserves());
                                otherResourcesTotal.setUnusable(otherResourcesTotal.getUnusable() + r.getUnusable());
                            });
        totals.setOtherResourcesTotal(otherResourcesTotal);

        Report allResourcesTotal = new Report();
        allResourcesTotal.setUsable(firstInterventionTotal.getUsable() + otherResourcesTotal.getUsable());
        allResourcesTotal.setReserves(firstInterventionTotal.getReserves() + otherResourcesTotal.getReserves());
        allResourcesTotal.setUnusable(firstInterventionTotal.getUnusable() + otherResourcesTotal.getUnusable());
        totals.setAllResourcesTotal(allResourcesTotal);

        Map<String, Report> equipmentTotals = report.getSubUnitReports()
                                                    .values()
                                                    .stream()
                                                    .flatMap(r -> r.getEquipmentReport().entrySet().stream())
                                                    .collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue(), (v1, v2) -> addReports(v1, v2)));
        totals.setEquipmentReport(equipmentTotals);
        Report equipmentsTotal = new Report();
        equipmentTotals.values()
                       .forEach(r -> {
                           equipmentsTotal.setUsable(equipmentsTotal.getUsable() + r.getUsable());
                           equipmentsTotal.setReserves(equipmentsTotal.getReserves() + r.getReserves());
                           equipmentsTotal.setUnusable(equipmentsTotal.getUnusable() + r.getUnusable());
                       });
        totals.setEquipmentTotal(equipmentsTotal);
    }

    private Report addReports(Report r1, Report r2) {
        Report r = new Report();
        r.setUsable(r1.getUsable() + r2.getUsable());
        r.setUnusable(r1.getUnusable() + r2.getUnusable());
        r.setReserves(r1.getReserves() + r2.getReserves());
        return r;
    }

    private void setAllResourcesTotal(SubUnitReport subUnitReport) {
        Report report = new Report();
        report.setUsable(subUnitReport.getFirstInterventionTotal().getUsable() + subUnitReport.getOtherResourcesTotal().getUsable());
        report.setReserves(subUnitReport.getFirstInterventionTotal().getReserves() + subUnitReport.getOtherResourcesTotal().getReserves());
        report.setUnusable(subUnitReport.getFirstInterventionTotal().getUnusable() + subUnitReport.getOtherResourcesTotal().getUnusable());
        subUnitReport.setAllResourcesTotal(report);
    }


    private void setFirstInterventionTotal(final SubUnitReport subUnitReport) {
        Report report = new Report();
        report.setUsable(subUnitReport.getFirstInterventionResourceReport()
                                      .values()
                                      .stream()
                                      .mapToLong(Report::getUsable)
                                      .sum());
        report.setReserves(subUnitReport.getFirstInterventionResourceReport()
                                        .values()
                                        .stream()
                                        .mapToLong(Report::getReserves)
                                        .sum());
        report.setUnusable(subUnitReport.getFirstInterventionResourceReport()
                                        .values()
                                        .stream()
                                        .mapToLong(Report::getUnusable)
                                        .sum());
        subUnitReport.setFirstInterventionTotal(report);
    }

    private void setOtherResourcesTotal(final SubUnitReport subUnitReport) {
        Report report = new Report();
        report.setUsable(subUnitReport.getOtherResourceReport()
                                      .values()
                                      .stream()
                                      .mapToLong(Report::getUsable)
                                      .sum());
        report.setReserves(subUnitReport.getOtherResourceReport()
                                        .values()
                                        .stream()
                                        .mapToLong(Report::getReserves)
                                        .sum());
        report.setUnusable(subUnitReport.getOtherResourceReport()
                                        .values()
                                        .stream()
                                        .mapToLong(Report::getUnusable)
                                        .sum());
        subUnitReport.setOtherResourcesTotal(report);
    }

    private void setEquipmentsTotal(final SubUnitReport subUnitReport) {
        Report report = new Report();
        report.setUsable(subUnitReport.getEquipmentReport()
                                      .values()
                                      .stream()
                                      .mapToLong(Report::getUsable)
                                      .sum());
        report.setReserves(subUnitReport.getEquipmentReport()
                                        .values()
                                        .stream()
                                        .mapToLong(Report::getReserves)
                                        .sum());
        report.setUnusable(subUnitReport.getEquipmentReport()
                                        .values()
                                        .stream()
                                        .mapToLong(Report::getUnusable)
                                        .sum());
        subUnitReport.setEquipmentTotal(report);
    }

    private Report buildReportForEquipment(final List<Equipment> equipmentList) {
        Report report = new Report();
        long available = equipmentList.stream()
                                      .mapToInt(Equipment::getUsable)
                                      .sum();
        long reserve = equipmentList.stream()
                                    .mapToInt(Equipment::getReserves)
                                    .sum();
        long unavailable = equipmentList.stream()
                                        .mapToInt(Equipment::getUnusable)
                                        .sum();
        report.setUsable(available);
        report.setReserves(reserve);
        report.setUnusable(unavailable);
        return report;
    }

    private Report buildReportForResource(final List<Resource> resourcesOfType) {
        Report report = new Report();
        long available = resourcesOfType.stream().filter(r -> !r.getType().equals(ResourceType.RESERVE)).count();
        long operationalReserves = resourcesOfType.stream()
                                                  .filter(r -> r.getType().equals(ResourceType.RESERVE)
                                                          && r.getStatus() != null
                                                          && r.getStatus().getStatus().equals(ResourceStatus.Status.OPERATIONAL)).count();
        long nonOperationalReserves = resourcesOfType.stream()
                                                     .filter(r -> r.getType().equals(ResourceType.RESERVE)
                                                             && r.getStatus() != null
                                                             && r.getStatus().getStatus().equals(ResourceStatus.Status.NONOPERATIONAL)).count();
        report.setUsable(available);
        report.setUnusable(nonOperationalReserves);
        report.setReserves(operationalReserves);
        return report;
    }

    private List<Equipment> getEquipmentOfType(final SubUnit subUnit, final String resourceType) {
        return subUnit.getEquipment()
                      .stream()
                      .filter(r -> r.getEquipmentType().equals(resourceType))
                      .collect(Collectors.toList());
    }

    private List<Resource> getResourcesOfType(final SubUnit subUnit, final String resourceType) {
        return subUnit.getResources()
                      .stream()
                      .filter(r -> r.getVehicleType().equals(resourceType))
                      .collect(Collectors.toList());
    }

    private List<VehicleType> getOtherResourcesTypes(List<SubUnit> subUnits, final List<VehicleType> vehicleTypeList) {
        return subUnits.stream()
                       .filter(s -> s.getResources() != null)
                       .flatMap(s -> s.getResources().stream())
                       .filter(resource -> resource.getType().equals(ResourceType.OTHER))
                       .map(Resource::getVehicleType)
                       .map(vehicleType -> getVehicleType(vehicleType, vehicleTypeList))
                       .distinct()
                       .sorted(getVehicleTypesComparator())
                       .collect(Collectors.toList());
    }

    private List<VehicleType> getFirstInterventionResourcesTypes(List<SubUnit> subUnits, final List<VehicleType> vehicleTypeList) {
        return subUnits.stream()
                       .flatMap(s -> s.getResources().stream())
                       .filter(resource -> resource.getType().equals(ResourceType.FIRST_INTERVENTION))
                       .map(Resource::getVehicleType)
                       .map(vehicleType -> getVehicleType(vehicleType, vehicleTypeList))
                       .distinct()
                       .sorted(getVehicleTypesComparator())
                       .collect(Collectors.toList());
    }

    private List<String> getEquipmentTypes(List<SubUnit> subUnits) {
        return subUnits.stream()
                       .filter(s -> s.getEquipment() != null)
                       .flatMap(s -> s.getEquipment().stream())
                       .map(Equipment::getEquipmentType)
                       .distinct()
                       .sorted()
                       .collect(Collectors.toList());
    }

    private VehicleType getVehicleType(final String shortName, final List<VehicleType> vehicleTypeList) {
        return vehicleTypeList.stream().filter(vehicleType -> vehicleType.getShortName().equals(shortName))
                              .findFirst()
                              .orElse(new VehicleType(UUID.randomUUID().toString(), shortName, shortName));
    }

    private Comparator<? super VehicleType> getVehicleTypesComparator() {
        return (Comparator<VehicleType>) (t1, t2) -> t1.getShortName().compareToIgnoreCase(t2.getShortName());
    }
}
