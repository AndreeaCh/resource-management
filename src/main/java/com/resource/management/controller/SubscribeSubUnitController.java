package com.resource.management.controller;

import com.resource.management.api.SubscribeSubUnitResponse;
import com.resource.management.data.SubUnit;
import com.resource.management.data.SubUnitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class SubscribeSubUnitController {
    private static final Logger LOG = LoggerFactory.getLogger(SubscribeSubUnitController.class);
    @Autowired
    private SubUnitRepository repository;

    @SubscribeMapping("/subunits")
    public SubscribeSubUnitResponse handleSubscribeMessage() {
        List<SubUnit> subUnits = repository.findAll();
        return new SubscribeSubUnitResponse(subUnits);
    }
}