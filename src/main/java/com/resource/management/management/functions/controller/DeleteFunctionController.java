package com.resource.management.management.functions.controller;

import com.resource.management.api.management.functions.DeleteFunctionRequest;
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

@Controller
public class DeleteFunctionController {
    @Autowired
    private FunctionsRepository repository;

    @MessageMapping("/deleteFunction")
    @SendTo("/topic/functions")
    public FunctionsListUpdatedNotification handle(final DeleteFunctionRequest request) {
        Optional<Functions> functions = repository.findById(Functions.ID);
        functions.ifPresent(f -> {
            Optional<Function> functionToDelete = f.getFunctions().stream()
                    .filter(func -> func.getId().equals(request.getId()))
                    .findFirst();

            if (functionToDelete.isPresent()) {
                f.getFunctions().remove(functionToDelete.get());
                repository.save(f);
            }
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
