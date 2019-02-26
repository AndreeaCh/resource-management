package com.resource.management.management.functions.controller;

import com.resource.management.api.management.functions.AddFunctionRequest;
import com.resource.management.api.management.functions.FunctionsListUpdatedNotification;
import com.resource.management.management.functions.model.Function;
import com.resource.management.management.functions.model.FunctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class AddFunctionController {
    @Autowired
    private FunctionRepository repository;

    @MessageMapping("/addFunction")
    @SendTo("/topic/functions")
    public FunctionsListUpdatedNotification handle(final AddFunctionRequest request) {
        Function function = new Function(UUID.randomUUID(), request.getName());
        repository.save(function);
        return new FunctionsListUpdatedNotification(repository.findAll());
    }
}
