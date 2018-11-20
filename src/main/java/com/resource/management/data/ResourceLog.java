package com.resource.management.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResourceLog {
    private String changedAt;

    private String changedBy;

    private ResourceStatus status;
}
