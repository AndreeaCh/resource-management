package com.resource.management.management.vehicles.model;

import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class VehicleType {
    @Id
    private String id;
    private String shortName;
    private String longName;
}
