package com.resource.management.management.functions.controller;

import com.resource.management.api.management.functions.AddFunctionRequest;
import com.resource.management.api.management.functions.FunctionsListUpdatedNotification;
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
import java.util.UUID;

@Controller
public class AddFunctionController {
    @Autowired
    private FunctionsRepository repository;

    @MessageMapping("/addFunction")
    @SendTo("/topic/functions")
    public FunctionsListUpdatedNotification handle(final AddFunctionRequest request) {
        Function function = new Function(UUID.randomUUID().toString(), request.getName());
        Optional<Functions> functions = repository.findById(Functions.ID);
        functions.ifPresent(f -> {
            f.getFunctions().add(function);
            repository.save(f);
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
