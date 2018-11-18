package com.resource.management.controller;

import com.resource.management.api.SubscribeSubUnitsResponse;
import com.resource.management.data.SubUnit;
import com.resource.management.data.SubUnitsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class SubscribeSubUnitsController {
    private static final Logger LOG = LoggerFactory.getLogger(SubscribeSubUnitsController.class);
    @Autowired
    private SubUnitsRepository repository;

    @SubscribeMapping("/subunits")
    public SubscribeSubUnitsResponse handleSubscribeMessage() {
        List<SubUnit> subUnits = repository.findAll();
        return new SubscribeSubUnitsResponse(subUnits);
    }
}