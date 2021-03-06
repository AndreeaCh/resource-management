package com.resource.management.resource.model;

public final class ResourceMapper {
    public static final Resource toInternal(final com.resource.management.api.resources.Resource externalResource) {
        Resource resource = new Resource();
        resource.setId(externalResource.getId());
        resource.setVehicleType(externalResource.getVehicleType());
        resource.setPlateNumber(externalResource.getPlateNumber());
        resource.setIdentificationNumber(externalResource.getIdentificationNumber());
        resource.setCaptain(externalResource.getCaptain());
        resource.setCrew(externalResource.getCrew());
        resource.setStatus(externalResource.getStatus());
        resource.setType(externalResource.getType());
        return resource;
    }

    public static final com.resource.management.api.resources.Resource toApi(final Resource internalResource) {
        com.resource.management.api.resources.Resource resource = new com.resource.management.api.resources.Resource();
        resource.setId(internalResource.getId());
        resource.setVehicleType(internalResource.getVehicleType());
        resource.setPlateNumber(internalResource.getPlateNumber());
        resource.setIdentificationNumber(internalResource.getIdentificationNumber());
        resource.setCaptain(internalResource.getCaptain());
        resource.setCrew(internalResource.getCrew());
        resource.setStatus(internalResource.getStatus());
        resource.setType(internalResource.getType());
        return resource;
    }
}
