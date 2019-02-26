package com.resource.management.management.functions.controller;

import com.resource.management.api.management.functions.FunctionsListUpdatedNotification;
import com.resource.management.api.management.functions.UpdateFunctionRequest;
import com.resource.management.api.services.ServicesListUpdatedNotification;
import com.resource.management.management.functions.model.Function;
import com.resource.management.management.functions.model.FunctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Optional;
import java.util.UUID;

@Controller
public class UpdateFunctionController {
    @Autowired
    private FunctionRepository repository;

    @MessageMapping("/updateFunction")
    @SendTo("/topic/functions")
    public FunctionsListUpdatedNotification handle(final UpdateFunctionRequest request) {
        if (request.getId() != null) {
            Optional<Function> functionOptional = repository.findById(request.getId());
            functionOptional.ifPresent(service -> {
                Function updatedFunction = new Function(UUID.fromString(request.getId()), request.getName());
                repository.save(updatedFunction);
            });
        }

        return new FunctionsListUpdatedNotification(repository.findAll());
    }
}
