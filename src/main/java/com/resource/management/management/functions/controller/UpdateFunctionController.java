package com.resource.management.management.functions.controller;

import com.resource.management.api.management.functions.FunctionsListUpdatedNotification;
import com.resource.management.api.management.functions.UpdateFunctionRequest;
import com.resource.management.management.functions.model.Function;
import com.resource.management.management.functions.service.FunctionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UpdateFunctionController {
    @Autowired
    private FunctionsService service;

    @MessageMapping("/updateFunction")
    @SendTo("/topic/functions")
    public FunctionsListUpdatedNotification handle(final UpdateFunctionRequest request) {
        List<Function> functionList = service.findAndUpdate(new Function(request.getId(), request.getName()));
        return new FunctionsListUpdatedNotification(functionList);
    }
}
