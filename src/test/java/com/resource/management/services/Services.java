package com.resource.management.services;

import com.resource.management.services.model.Service;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.Instant;
import java.util.UUID;

public class Services {
    public static Service api() {
        return new Service(UUID.randomUUID().toString(),
                RandomStringUtils.randomAlphabetic(1, 100),
                RandomStringUtils.randomAlphabetic(1, 100),
                RandomStringUtils.randomAlphabetic(1, 100),
                RandomStringUtils.randomAlphabetic(1, 100),
                Instant.now().toString());
    }
}
