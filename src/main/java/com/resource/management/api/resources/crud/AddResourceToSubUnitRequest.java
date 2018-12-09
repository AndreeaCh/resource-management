package com.resource.management.api.resources.crud;

import com.resource.management.api.resources.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddResourceToSubUnitRequest {
    private String subUnitName;
    private Resource resource;
}
