package com.resource.management.management.functions.controller;

import com.resource.management.api.management.functions.FunctionsListUpdatedNotification;
import com.resource.management.management.functions.model.Function;
import com.resource.management.management.functions.model.Functions;
import com.resource.management.management.functions.model.FunctionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class SubscribeForFunctionsListController {
    @Autowired
    private FunctionsRepository repository;

    @SubscribeMapping("/functions")
    public FunctionsListUpdatedNotification handle() {
        return getFunctionsListUpdatedNotification();
    }

    private FunctionsListUpdatedNotification getFunctionsListUpdatedNotification() {
        Optional<Functions> functions;
        List<Function> functionList = new ArrayList<>();
        functions = repository.findById(Functions.ID);
        if (functions.isPresent()) {
            functionList = functions.get().getFunctions();
        }
        return new FunctionsListUpdatedNotification(functionList);
    }
}
