package com.resource.management;

import com.resource.management.model.Resource;
import com.resource.management.model.ResourceLog;
import com.resource.management.model.ResourceStatus;

import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

public final class ResourcesTestData {
    private ResourcesTestData() {
        // prevent instantiation
    }


    public static Resource randomInternal() {
        ResourceLog resourceLog =
                new ResourceLog(UUID.randomUUID(), Instant.now().toString(), "10.12.12.12", new ResourceStatus(ResourceStatus.Status.AVAILABLE));
        Resource resource = new Resource();
        resource.setIdentificationNumber("1");
        resource.setPlateNumber("CJ02AKD");
        resource.setResourceLogs(Collections.singletonList(resourceLog));
        return resource;
    }


    public static com.resource.management.api.Resource randomApi() {
        com.resource.management.api.ResourceLog resourceLog =
                new com.resource.management.api.ResourceLog(Instant.now().toString(), "10.12.12.12",
                        new ResourceStatus(ResourceStatus.Status.AVAILABLE));
        com.resource.management.api.Resource resource = new com.resource.management.api.Resource();
        resource.setIdentificationNumber("1");
        resource.setPlateNumber("CJ02AKD");
        ;
        return resource;
    }
}
