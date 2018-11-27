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
public class AddSubUnitRequest {
    private SubUnit subUnit;
}
