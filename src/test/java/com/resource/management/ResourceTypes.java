package com.resource.management;

import com.resource.management.resource.model.ResourceType;

import java.util.Random;

public class ResourceTypes {

    public static ResourceType randomResourceType() {
        Random r = new Random();
        return ResourceType.values()[r.nextInt(ResourceType.values().length)];
    }
}
