package com.resource.management.services.controller;

import com.resource.management.api.services.ServicesListUpdatedNotification;
import com.resource.management.services.model.Service;
import com.resource.management.services.model.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Controller
public class GetServicesListController {
    @Autowired
    private ServiceRepository repository;

    @SubscribeMapping("/services")
    public ServicesListUpdatedNotification handle() {
        List<Service> services = repository.findAll();
        Instant lastUpdate = services.stream().map(s -> Instant.parse(s.getLastUpdate())).max(Comparator.naturalOrder()).orElse(null);
        return new ServicesListUpdatedNotification(services, lastUpdate != null ? lastUpdate.toString() : null);

    }
}