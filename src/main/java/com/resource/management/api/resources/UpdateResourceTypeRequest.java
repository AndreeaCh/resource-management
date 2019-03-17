package com.resource.management.api.resources;

import com.resource.management.resource.model.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateResourceTypeRequest {

    private String plateNumber;

    private ResourceType resourceType;
}
