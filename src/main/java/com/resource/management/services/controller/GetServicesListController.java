package com.resource.management.services.controller;

import com.resource.management.api.services.ServicesListUpdatedNotification;
import com.resource.management.services.model.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class GetServicesListController {
    @Autowired
    private ServiceRepository repository;

    @SubscribeMapping("/services")
    public ServicesListUpdatedNotification handle() {
        return new ServicesListUpdatedNotification(repository.findAll());
    }
}