package com.resource.management;

import com.resource.management.resource.model.SubUnit;

public class SubUnits {
    public static SubUnit internal() {
        return JsonUtils.loadFromJsonString(SubUnit.class, SubUnitsTestData.SUBUNIT1);
    }

    public static SubUnit updatedInternal() {
        return JsonUtils.loadFromJsonString(SubUnit.class, SubUnitsTestData.SUBUNIT1_UPDATED);
    }

    public static com.resource.management.api.resources.SubUnit api() {
        return JsonUtils.loadFromJsonString(com.resource.management.api.resources.SubUnit.class, SubUnitsTestData.SUBUNIT1);
    }

    public static com.resource.management.api.resources.SubUnit updatedApi() {
        return JsonUtils.loadFromJsonString(com.resource.management.api.resources.SubUnit.class, SubUnitsTestData.SUBUNIT1_UPDATED);
    }
}
