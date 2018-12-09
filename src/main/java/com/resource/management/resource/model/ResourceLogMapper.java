package com.resource.management.resource.model;


public final class ResourceLogMapper {
    public static final ResourceLog toInternal(final com.resource.management.api.resources.ResourceLog externalResourceLog) {
        ResourceLog resourceLog = new ResourceLog();
        resourceLog.setStatus(externalResourceLog.getStatus());
        resourceLog.setChangedAt(externalResourceLog.getChangedAt());
        resourceLog.setChangedBy(externalResourceLog.getChangedBy());
        return resourceLog;
    }

    public static final com.resource.management.api.resources.ResourceLog toApi(final ResourceLog internalResourceLog) {
        com.resource.management.api.resources.ResourceLog resourceLog = new com.resource.management.api.resources.ResourceLog();
        resourceLog.setStatus(internalResourceLog.getStatus());
        resourceLog.setChangedAt(internalResourceLog.getChangedAt());
        resourceLog.setChangedBy(internalResourceLog.getChangedBy());
        return resourceLog;
    }
}
