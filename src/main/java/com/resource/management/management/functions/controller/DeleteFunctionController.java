package com.resource.management.management.functions.controller;

import com.resource.management.api.management.functions.DeleteFunctionRequest;
import com.resource.management.api.management.functions.FunctionsListUpdatedNotification;
import com.resource.management.management.functions.model.FunctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class DeleteFunctionController {
    @Autowired
    private FunctionRepository repository;

    @MessageMapping("/deleteFunction")
    @SendTo("/topic/functions")
    public FunctionsListUpdatedNotification handle(final DeleteFunctionRequest request) {
        repository.deleteById(request.getId());
        return new FunctionsListUpdatedNotification(repository.findAll());
    }
}
