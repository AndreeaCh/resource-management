package com.resource.management.resource.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ResourceLog {
    private UUID id;

    private String changedAt;

    private String changedBy;

    private ResourceType resourceType;

    private ResourceStatus status;


    @Override
    public String toString() {
        return "Data&ora='" + changedAt + '\'' + ", IP='" + changedBy + '\'' + this.resourceType != null ? ", " +
                status + ", " : "" + this.status != null ? ", " + status : "";
    }
}
