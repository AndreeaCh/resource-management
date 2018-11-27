package com.resource.management.model;


public final class ResourceLogMapper {
    public static final ResourceLog toInternal(final com.resource.management.api.ResourceLog externalResourceLog) {
        ResourceLog resourceLog = new ResourceLog();
        resourceLog.setStatus(externalResourceLog.getStatus());
        resourceLog.setChangedAt(externalResourceLog.getChangedAt());
        resourceLog.setChangedBy(externalResourceLog.getChangedBy());
        return resourceLog;
    }

    public static final com.resource.management.api.ResourceLog toApi(final ResourceLog internalResourceLog) {
        com.resource.management.api.ResourceLog resourceLog = new com.resource.management.api.ResourceLog();
        resourceLog.setStatus(internalResourceLog.getStatus());
        resourceLog.setChangedAt(internalResourceLog.getChangedAt());
        resourceLog.setChangedBy(internalResourceLog.getChangedBy());
        return resourceLog;
    }
}
