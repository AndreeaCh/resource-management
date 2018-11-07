package com.resource.management.data;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;

public class Detachment {
    @Id
    private final String name;

    public Detachment(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .toString();
    }
}
