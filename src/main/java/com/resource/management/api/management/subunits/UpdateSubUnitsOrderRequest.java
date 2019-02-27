package com.resource.management.api.management.subunits;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateSubUnitsOrderRequest {
    private List<String> subUnitIds;
}
