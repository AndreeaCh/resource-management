package com.resource.management.management.trucks.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Trucks {
    public static final String ID = "Trucks";
    @Id
    private String id;
    private List<Truck> trucks;
}
