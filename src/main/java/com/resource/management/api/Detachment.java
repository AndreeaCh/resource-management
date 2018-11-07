package com.resource.management.api;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Detachment {
    private String name;

    public Detachment() {
    }

    public Detachment(String name) {
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
