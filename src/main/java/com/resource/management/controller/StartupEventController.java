package com.resource.management.controller;

import com.resource.management.service.SubUnitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class StartupEventController {
    @Autowired
    private SubUnitsService service;

    @PostConstruct
    public void init() {
        // If the server is closed abnormally then some resources might remain locked in the DB. As a safety measure, at startup we unlock everything.
        service.unlockAllSubUnits();
    }
}
