/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package com.resource.management.controller;

import com.resource.management.data.SubUnitsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class StartupEventController {
    @Autowired
    private SubUnitsRepository repository;

    @PostConstruct
    public void init() {
        // If the server is closed abnormally then some resources might remain locked in the DB. As a safety measure, at startup we unlock everything.
        repository.findAll().forEach(s -> {
            s.setIsLocked(false);
            s.setLockedBy(null);
            repository.save(s);
        });
    }
}
