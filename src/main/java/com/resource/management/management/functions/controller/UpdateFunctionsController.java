package com.resource.management.management.functions.controller;

import com.resource.management.api.management.functions.FunctionsListUpdatedNotification;
import com.resource.management.api.management.functions.UpdateFunctionsRequest;
import com.resource.management.management.functions.model.Function;
import com.resource.management.management.functions.model.Functions;
import com.resource.management.management.functions.model.FunctionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class UpdateFunctionsController {
    @Autowired
    private FunctionsRepository repository;

    @MessageMapping("/updateFunctions")
    @SendTo("/topic/functions")
    public FunctionsListUpdatedNotification handle(final UpdateFunctionsRequest request) {
        Optional<Functions> functions = repository.findById(Functions.ID);
        functions.ifPresent(f -> {
            Functions storedFunctions = functions.get();
            storedFunctions.setFunctions(request.getFunctions());
            repository.save(storedFunctions);
        });

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
