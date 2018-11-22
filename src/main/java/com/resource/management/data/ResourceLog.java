package com.resource.management.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class ResourceLog {
    @JsonIgnore
    private UUID id;

    private String changedAt;

    private String changedBy;

    private ResourceStatus status;
}
