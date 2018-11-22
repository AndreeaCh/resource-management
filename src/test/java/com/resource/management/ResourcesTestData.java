package com.resource.management;

import com.resource.management.data.Resource;
import com.resource.management.data.ResourceLog;
import com.resource.management.data.ResourceStatus;

import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

public final class ResourcesTestData {
    private ResourcesTestData() {
        // prevent instantiation
    }

    public static Resource random() {
        ResourceLog resourceLog = new ResourceLog(UUID.randomUUID(), Instant.now().toString(), "10.12.12.12", ResourceStatus.AVAILABLE_ON_ROUTE);
        Resource resource = new Resource();
        resource.setIdentificationNumber("1");
        resource.setPlateNumber("CJ02AKD");
        resource.setResourceLogs(Collections.singletonList(resourceLog));
        return resource;
    }
}
