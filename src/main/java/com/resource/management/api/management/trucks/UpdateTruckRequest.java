package com.resource.management.api.management.trucks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateTruckRequest {
    private String id;
    private String shortName;
    private String longName;
}
