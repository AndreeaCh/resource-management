/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package com.resource.management.controller;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Controller
public class ConnectController {
    @EventListener
    public void onConnectEvent(final SessionConnectedEvent event) {

    }
}
