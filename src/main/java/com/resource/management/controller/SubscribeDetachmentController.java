package com.resource.management.controller;

import com.resource.management.api.SubscribeDetachmentsRequest;
import com.resource.management.api.SubscribeDetachmentsResponse;
import com.resource.management.data.DetachmentsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SubscribeDetachmentController {
    private static final Logger LOG = LoggerFactory.getLogger(SubscribeDetachmentController.class);
    @Autowired
    private DetachmentsRepository repository;

    @MessageMapping("/subscribeDetachment")
    @SendTo("/topic/detachments")
    public SubscribeDetachmentsResponse subscribeForDetachments(final SubscribeDetachmentsRequest request) throws Exception {
        LOG.info("Received message: " + request);
        return new SubscribeDetachmentsResponse("CJ");
    }
}