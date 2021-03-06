package com.resource.management.api.resources.lock;

import com.resource.management.resource.model.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LockSubUnitRequest {

    private String subUnitId;
    private ResourceType resourceType;
}
