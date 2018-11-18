package com.resource.management.api;

import com.resource.management.data.SubUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class AddSubUnitRequest {
    private SubUnit subUnit;
}
