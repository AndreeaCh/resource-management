package com.resource.management.resource.model;

import com.resource.management.services.model.Service;

public class ServiceMapper {

    public static final Service toInternal(final com.resource.management.api.resources.Service externalService) {
        Service internalService = new Service();
        internalService.setId(externalService.getId());
        internalService.setTitle(externalService.getId());
        internalService.setRole(externalService.getId());
        internalService.setContact(externalService.getId());

        return internalService;
    }

    public static final com.resource.management.api.resources.Service toApi(final Service internalService) {
        com.resource.management.api.resources.Service externalService = new com.resource.management.api.resources.Service();
        externalService.setId(internalService.getId());
        externalService.setTitle(internalService.getTitle());
        externalService.setRole(internalService.getRole());
        externalService.setContact(internalService.getContact());

        return externalService;
    }
}
