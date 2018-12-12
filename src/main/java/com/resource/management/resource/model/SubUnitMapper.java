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
        subUnit.setName(internalSubUnit.getName());
        subUnit.setIsLocked(internalSubUnit.getIsLocked());
        subUnit.setLastUpdate(internalSubUnit.getLastUpdate());
        if (internalSubUnit.getResources() != null) {
            List<Resource> resourceList = internalSubUnit
                    .getResources()
                    .stream()
                    .map(ResourceMapper::toApi)
                    .collect(Collectors.toList());

            subUnit.setResources(resourceList);
        }

        return subUnit;
    }

    public static com.resource.management.resource.model.SubUnit toInternal(final SubUnit externalSubUnit) {
        com.resource.management.resource.model.SubUnit subUnit = new com.resource.management.resource.model.SubUnit();
        subUnit.setName(externalSubUnit.getName());
        subUnit.setLastUpdate(externalSubUnit.getLastUpdate());
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
