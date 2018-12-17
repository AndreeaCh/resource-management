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
public class UnlockSubUnitRequest {

    private String subUnitName;
    private ResourceType resourceType;
}
