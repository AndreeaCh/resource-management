package com.resource.management.api.resources.lock;

import com.resource.management.resource.model.ResourceType;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SubUnitLockedNotification {
    private String subUnitId;
    private Set<ResourceType> lockedResourceTypes;
}
