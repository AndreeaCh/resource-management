package com.resource.management.management.vehicles.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class VehicleTypes {
    public static final String ID = "vehicleTypes";
    @Id
    private String id;
    private List<VehicleType> vehicleTypes;
}
