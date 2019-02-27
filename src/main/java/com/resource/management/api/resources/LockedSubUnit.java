package com.resource.management.api.resources;

import com.resource.management.resource.model.ResourceType;
import lombok.*;

import java.util.Set;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LockedSubUnit {
    private String subUnitId;
    private Set<ResourceType> lockedResourceTypes;
}
