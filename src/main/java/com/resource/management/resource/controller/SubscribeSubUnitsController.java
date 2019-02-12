package com.resource.management.resource.controller;

import com.resource.management.resource.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class SubscribeSubUnitsController {

    @Autowired
    private NotificationService notificationService;

    @SubscribeMapping("/subunits")
    public void handleSubscribeMessage() {
        notificationService.publishInitialSubUnitsNotification();
    }
}