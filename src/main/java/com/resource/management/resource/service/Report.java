package com.resource.management.resource.service;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Report {
    private long usable;

    private long unusable;

    private long reserves;

    public String getUsableAsString() {
        return Long.toString(usable);
    }

    public String getUnusableAsString() {
        return Long.toString(unusable);
    }

    public String getReserveAsString() {
        return Long.toString(reserves);
    }
}
