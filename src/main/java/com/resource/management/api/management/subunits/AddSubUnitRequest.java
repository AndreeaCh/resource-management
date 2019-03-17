package com.resource.management.api.management.subunits;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddSubUnitRequest {
    private String id;
    private String name;
}
