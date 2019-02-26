package com.resource.management.management.functions.controller;

import com.resource.management.api.management.functions.FunctionsListUpdatedNotification;
import com.resource.management.management.functions.model.Function;
import com.resource.management.management.functions.model.FunctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class SubscribeForFunctionsListController {
    @Autowired
    private FunctionRepository repository;

    @SubscribeMapping("/functions")
    public FunctionsListUpdatedNotification handle() {
        List<Function> functions = repository.findAll();
        return new FunctionsListUpdatedNotification(functions);
    }
}
