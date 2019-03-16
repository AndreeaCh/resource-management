package com.resource.management.resource.model;

import com.resource.management.api.resources.Resource;
import com.resource.management.api.resources.SubUnit;

import java.util.List;
import java.util.stream.Collectors;

public final class SubUnitMapper {
    public static List<SubUnit> toApi(List<com.resource.management.resource.model.SubUnit> internalSubUnits) {
        return internalSubUnits.stream().map(SubUnitMapper::toApi).collect(Collectors.toList());
    }

    public static SubUnit toApi(com.resource.management.resource.model.SubUnit internalSubUnit) {
        SubUnit subUnit = new SubUnit();
        subUnit.setId(internalSubUnit.getId());
        subUnit.setName(internalSubUnit.getName());
        subUnit.setLastUpdateFirstInterventionResource(internalSubUnit.getLastUpdateFirstInterventionResource());
        subUnit.setLastUpdateOtherResource(internalSubUnit.getLastUpdateOtherResource());
        subUnit.setLastUpdateEquipment(internalSubUnit.getLastUpdateEquipment());
        subUnit.setLastUpdateReserveResource(internalSubUnit.getLastUpdateReserveResource());
        if (internalSubUnit.getResources() != null) {
            List<Resource> resourceList = internalSubUnit
                    .getResources()
                    .stream()
                    .map(ResourceMapper::toApi)
                    .collect(Collectors.toList());

            subUnit.setResources(resourceList);
        }

        if (internalSubUnit.getEquipment() != null) {
            List<com.resource.management.api.resources.Equipment> equipmentList = internalSubUnit
                    .getEquipment()
                    .stream()
                    .map(EquipmentMapper::toApi)
                    .collect(Collectors.toList());
            subUnit.setEquipment(equipmentList);
        }

        return subUnit;
    }

    public static com.resource.management.resource.model.SubUnit toInternal(final SubUnit externalSubUnit) {
        com.resource.management.resource.model.SubUnit subUnit = new com.resource.management.resource.model.SubUnit();
        subUnit.setId(externalSubUnit.getId());
        subUnit.setName(externalSubUnit.getName());
        subUnit.setLastUpdateFirstInterventionResource(externalSubUnit.getLastUpdateFirstInterventionResource());
        subUnit.setLastUpdateOtherResource(externalSubUnit.getLastUpdateOtherResource());
        subUnit.setLastUpdateEquipment(externalSubUnit.getLastUpdateEquipment());
        subUnit.setLastUpdateReserveResource(externalSubUnit.getLastUpdateReserveResource());
        if (externalSubUnit.getResources() != null) {
            List<com.resource.management.resource.model.Resource> resourceList = externalSubUnit
                    .getResources()
                    .stream()
                    .map(ResourceMapper::toInternal)
                    .collect(Collectors.toList());
            subUnit.setResources(resourceList);
        }

        if (externalSubUnit.getEquipment() != null) {
            List<com.resource.management.resource.model.Equipment> equipmentList = externalSubUnit
                    .getEquipment()
                    .stream()
                    .map(EquipmentMapper::toInternal)
                    .collect(Collectors.toList());
            subUnit.setEquipment(equipmentList);
        }
        return subUnit;
    }
}
