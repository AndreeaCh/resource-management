package com.resource.management.model;

import com.resource.management.api.Resource;
import com.resource.management.api.SubUnit;

import java.util.List;
import java.util.stream.Collectors;

public final class SubUnitMapper {
    public static List<SubUnit> toApi(List<com.resource.management.model.SubUnit> internalSubUnits) {
        return internalSubUnits.stream().map(SubUnitMapper::toApi).collect(Collectors.toList());
    }

    public static SubUnit toApi(com.resource.management.model.SubUnit internalSubUnit) {
        SubUnit subUnit = new SubUnit();
        subUnit.setName(internalSubUnit.getName());
        subUnit.setIsLocked(internalSubUnit.getIsLocked());
        subUnit.setLastUpdate(internalSubUnit.getLastUpdate());
        List<Resource> resourceList = internalSubUnit
                .getResources()
                .stream()
                .map(ResourceMapper::toApi)
                .collect(Collectors.toList());

        subUnit.setResources(resourceList);
        return subUnit;
    }

    public static com.resource.management.model.SubUnit toInternal(final SubUnit externalSubUnit) {
        com.resource.management.model.SubUnit subUnit = new com.resource.management.model.SubUnit();
        subUnit.setName(externalSubUnit.getName());
        subUnit.setLastUpdate(externalSubUnit.getLastUpdate());
        List<com.resource.management.model.Resource> resourceList = externalSubUnit
                .getResources()
                .stream()
                .map(ResourceMapper::toInternal)
                .collect(Collectors.toList());

        subUnit.setResources(resourceList);
        return subUnit;
    }
}
