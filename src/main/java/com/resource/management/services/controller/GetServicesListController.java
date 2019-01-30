package com.resource.management.services.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import com.resource.management.api.services.ServicesListUpdatedNotification;
import com.resource.management.services.model.LastUpdatedTimestamp;
import com.resource.management.services.model.LastUpdatedTimestampRepository;
import com.resource.management.services.model.Service;
import com.resource.management.services.model.ServiceRepository;

@Controller
public class GetServicesListController {
    @Autowired
    private ServiceRepository repository;

    @Autowired
    private LastUpdatedTimestampRepository timestampRepository;

    @SubscribeMapping("/services")
    public ServicesListUpdatedNotification handle() {
        List<Service> services = repository.findAll();

        final ServicesListUpdatedNotification notification;
        final Optional<LastUpdatedTimestamp> timestampOptional = timestampRepository.findById( "timeStamp" );
        notification = timestampOptional.map( lastUpdatedTimestamp -> new ServicesListUpdatedNotification(
              services, lastUpdatedTimestamp.getTimeStamp() ) ).orElseGet( () -> new ServicesListUpdatedNotification(
              services, null ) );

        return notification;

    }
}
