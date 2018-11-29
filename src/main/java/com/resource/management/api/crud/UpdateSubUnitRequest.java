package com.resource.management.api.crud;

import com.resource.management.api.SubUnit;
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
