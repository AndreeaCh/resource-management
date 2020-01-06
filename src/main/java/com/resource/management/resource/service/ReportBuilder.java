package com.resource.management.resource.service;

import com.resource.management.management.vehicles.model.VehicleType;
import com.resource.management.resource.model.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ReportBuilder {
    public AllSubUnitsReport buildReport(final List<SubUnit> subUnits, final List<VehicleType> vehicleTypeList) {
        AllSubUnitsReport report = new AllSubUnitsReport();
        List<SubUnitReport> subUnitReports = new ArrayList<>();
        report.setSubUnitReports(subUnitReports);

        subUnits.forEach(subUnit -> {
            SubUnitReport subUnitReport = new SubUnitReport();
            subUnitReport.setSubUnitName(subUnit.getName());
            List<VehicleType> firstInterventionResourcesTypes = getFirstInterventionResourcesTypes(subUnits, vehicleTypeList);
            List<VehicleTypeReport> fiResources = new ArrayList<>();
            firstInterventionResourcesTypes.forEach(resourceType -> {
                List<Resource> resourcesOfType = getResourcesOfType(subUnit, resourceType.getShortName());
                fiResources.add(new VehicleTypeReport(resourceType, buildReportForResource(resourcesOfType)));
            });
            subUnitReport.setFirstInterventionResourceReport(fiResources);

            List<VehicleTypeReport> otherResources = new ArrayList<>();
            List<VehicleType> otherResourcesTypes = getOtherResourcesTypes(subUnits, vehicleTypeList);
            otherResourcesTypes.forEach(resourceType -> {
                List<Resource> resourcesOfType = getResourcesOfType(subUnit, resourceType.getShortName());
                otherResources.add(new VehicleTypeReport(resourceType, buildReportForResource(resourcesOfType)));
            });
            subUnitReport.setOtherResourceReport(otherResources);

            List<EquipmentReport> equipmentResourceMap = new ArrayList<>();
            List<Equipment> equipmentResourceTypes = getEquipmentTypes(subUnits);
            equipmentResourceTypes.forEach(equipment -> {
                List<Equipment> equipmentList = getEquipmentOfType(subUnit, equipment.getEquipmentType());
                equipmentResourceMap.add(new EquipmentReport(equipment, buildReportForEquipment(equipmentList)));
            });
            subUnitReport.setEquipmentReport(equipmentResourceMap);

            setFirstInterventionTotal(subUnitReport);
            setOtherResourcesTotal(subUnitReport);
            setAllResourcesTotal(subUnitReport);
            setEquipmentsTotal(subUnitReport);
            subUnitReports.add(subUnitReport);
        });

        // computeTotals(report);
        return report;
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
                .stream()
                .map(VehicleTypeReport::getReport)
                .mapToLong(Report::getUsable)
                .sum());
        report.setReserves(subUnitReport.getFirstInterventionResourceReport()
                .stream()
                .map(VehicleTypeReport::getReport)
                .mapToLong(Report::getReserves)
                .sum());
        report.setUnusable(subUnitReport.getFirstInterventionResourceReport()
                .stream()
                .map(VehicleTypeReport::getReport)
                .mapToLong(Report::getUnusable)
                .sum());
        subUnitReport.setFirstInterventionTotal(report);
    }

    private void setOtherResourcesTotal(final SubUnitReport subUnitReport) {
        Report report = new Report();
        report.setUsable(subUnitReport.getOtherResourceReport()
                .stream()
                .map(VehicleTypeReport::getReport)
                .mapToLong(Report::getUsable)
                .sum());
        report.setReserves(subUnitReport.getOtherResourceReport()
                .stream()
                .map(VehicleTypeReport::getReport)
                .mapToLong(Report::getReserves)
                .sum());
        report.setUnusable(subUnitReport.getOtherResourceReport()
                .stream()
                .map(VehicleTypeReport::getReport)
                .mapToLong(Report::getUnusable)
                .sum());
        subUnitReport.setOtherResourcesTotal(report);
    }

    private void setEquipmentsTotal(final SubUnitReport subUnitReport) {
        Report report = new Report();
        report.setUsable(subUnitReport.getEquipmentReport()
                .stream()
                .map(EquipmentReport::getReport)
                .mapToLong(Report::getUsable)
                .sum());
        report.setReserves(subUnitReport.getEquipmentReport()
                .stream()
                .map(EquipmentReport::getReport)
                .mapToLong(Report::getReserves)
                .sum());
        report.setUnusable(subUnitReport.getEquipmentReport()
                .stream()
                .map(EquipmentReport::getReport)
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
                .filter(distinctByKey(VehicleType::getShortName))
                .sorted(getVehicleTypesComparator())
                .collect(Collectors.toList());
    }

    private List<VehicleType> getFirstInterventionResourcesTypes(List<SubUnit> subUnits, final List<VehicleType> vehicleTypeList) {
        return subUnits.stream()
                .flatMap(s -> s.getResources().stream())
                .filter(resource -> resource.getType().equals(ResourceType.FIRST_INTERVENTION))
                .map(Resource::getVehicleType)
                .map(vehicleType -> getVehicleType(vehicleType, vehicleTypeList))
                .filter(distinctByKey(VehicleType::getShortName))
                .sorted(getVehicleTypesComparator())
                .collect(Collectors.toList());
    }

    private List<Equipment> getEquipmentTypes(List<SubUnit> subUnits) {
        return subUnits.stream()
                .filter(s -> s.getEquipment() != null)
                .flatMap(s -> s.getEquipment().stream())
                .filter(distinctByKey(Equipment::getEquipmentType))
                .sorted(getEquipmentComparator())
                .collect(Collectors.toList());
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    private Comparator<? super Equipment> getEquipmentComparator() {
        return (Comparator<Equipment>) (t1, t2) -> t1.getEquipmentType().compareToIgnoreCase(t2.getEquipmentType());
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
