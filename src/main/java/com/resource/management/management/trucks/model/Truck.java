package com.resource.management.management.trucks.model;

import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Truck {
    @Id
    private String id;
    private String shortName;
    private String longName;
}
