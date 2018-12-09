package com.resource.management.api.resources.crud;

import com.resource.management.api.resources.SubUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateSubUnitRequest {
    private SubUnit subUnit;
}
