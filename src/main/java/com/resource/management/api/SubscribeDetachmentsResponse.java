package com.resource.management.api;

import org.apache.commons.lang.builder.ToStringBuilder;

public class SubscribeDetachmentsResponse {
    private String name;

    public SubscribeDetachmentsResponse() {
    }

    public SubscribeDetachmentsResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .toString();
    }
}
