package com.resource.management.resource.controller;

import com.resource.management.api.resources.crud.notifications.InitialSubUnitsNotification;
import com.resource.management.resource.model.SubUnit;
import com.resource.management.resource.model.SubUnitMapper;
import com.resource.management.resource.model.SubUnitsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class SubscribeSubUnitsController {

    @Autowired
    private SubUnitsRepository repository;

    @SubscribeMapping("/subunits")
    public InitialSubUnitsNotification handleSubscribeMessage() {
        List<SubUnit> subUnits = repository.findAll();
        List<com.resource.management.api.resources.SubUnit> subUnitsList = SubUnitMapper.toApi(subUnits);
        InitialSubUnitsNotification initialSubUnitsNotification = new InitialSubUnitsNotification(subUnitsList);
        return initialSubUnitsNotification;
    }
}