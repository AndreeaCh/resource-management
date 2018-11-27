package com.resource.management.controller;

import com.resource.management.api.crud.notifications.InitialSubUnitsNotification;
import com.resource.management.model.SubUnit;
import com.resource.management.model.SubUnitsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class SubscribeSubUnitsController {
    private static final Logger LOG = LoggerFactory.getLogger(SubscribeSubUnitsController.class);
    @SubscribeMapping("/subunits")
    public InitialSubUnitsNotification handleSubscribeMessage() {
        List<SubUnit> subUnits = repository.findAll();
        return new InitialSubUnitsNotification(subUnits);
    }

    @Autowired
    private SubUnitsRepository repository;
}