package com.resource.management;

import com.resource.management.model.SubUnit;

import java.util.List;
import java.util.stream.Collectors;

public class SubUnits {
    public static SubUnit internal() {
        return JsonUtils.loadFromJsonString(SubUnit.class, SubUnitsTestData.SUBUNIT1);
    }

    public static SubUnit updatedInternal() {
        return JsonUtils.loadFromJsonString(SubUnit.class, SubUnitsTestData.SUBUNIT1_UPDATED);
    }

    public static com.resource.management.api.SubUnit api() {
        return JsonUtils.loadFromJsonString(com.resource.management.api.SubUnit.class, SubUnitsTestData.SUBUNIT1);
    }

    public static com.resource.management.api.SubUnit updatedApi() {
        return JsonUtils.loadFromJsonString(com.resource.management.api.SubUnit.class, SubUnitsTestData.SUBUNIT1_UPDATED);
    }

    public static List<SubUnit> loadAllSubUnits() {
        return SubUnitsTestData.ALL_UNITS.stream()
                .map(subUnit -> JsonUtils.loadFromJsonString(SubUnit.class, subUnit)).collect(Collectors.toList());
    }

}
