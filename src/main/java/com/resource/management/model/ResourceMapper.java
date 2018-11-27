package com.resource.management.model;


public final class ResourceMapper {
    public static final Resource toInternal(final com.resource.management.api.Resource externalResource) {
        Resource resource = new Resource();
        resource.setVehicleType(externalResource.getVehicleType());
        resource.setPlateNumber(externalResource.getPlateNumber());
        resource.setIdentificationNumber(externalResource.getIdentificationNumber());
        resource.setCaptain(externalResource.getCaptain());
        resource.setCrew(externalResource.getCrew());
        resource.setStatus(externalResource.getStatus());
        return resource;
    }

    public static final com.resource.management.api.Resource toApi(final Resource internalResource) {
        com.resource.management.api.Resource resource = new com.resource.management.api.Resource();
        resource.setVehicleType(internalResource.getVehicleType());
        resource.setPlateNumber(internalResource.getPlateNumber());
        resource.setIdentificationNumber(internalResource.getIdentificationNumber());
        resource.setCaptain(internalResource.getCaptain());
        resource.setCrew(internalResource.getCrew());
        resource.setStatus(internalResource.getStatus());
        return resource;
    }
}
