package com.resource.management;

import com.resource.management.resource.model.Resource;
import com.resource.management.resource.model.ResourceLog;
import com.resource.management.resource.model.ResourceStatus;

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


    public static com.resource.management.api.resources.Resource randomApi() {
        com.resource.management.api.resources.ResourceLog resourceLog =
                new com.resource.management.api.resources.ResourceLog(Instant.now().toString(), "10.12.12.12",
                        new ResourceStatus(ResourceStatus.Status.AVAILABLE));
        com.resource.management.api.resources.Resource resource = new com.resource.management.api.resources.Resource();
        resource.setIdentificationNumber("1");
        resource.setPlateNumber("CJ02AKD");
        ;
        return resource;
    }
}
