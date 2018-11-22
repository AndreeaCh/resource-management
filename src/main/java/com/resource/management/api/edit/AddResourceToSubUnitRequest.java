package com.resource.management.api.edit;

import com.resource.management.data.Resource;
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
