package com.resource.management.resource.model;

import com.resource.management.services.model.Service;

public class ServiceMapper {

    public static final Service toInternal(final com.resource.management.api.resources.Service externalService,
                                           String lastUpdate) {
        Service internalService = new Service();
        internalService.setId(externalService.getId());
        internalService.setName(externalService.getName());
        internalService.setTitle(externalService.getTitle());
        internalService.setRole(externalService.getRole());
        internalService.setContact(externalService.getContact());
        internalService.setLastUpdate(lastUpdate);
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
