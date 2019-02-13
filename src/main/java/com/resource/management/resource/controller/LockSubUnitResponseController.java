package com.resource.management.resource.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class LockSubUnitResponseController {
    @MessageMapping("/lockSubUnitResponse")
    public void handleLockSubUnitMessage() {
        //Just manage subscription
    }
}
