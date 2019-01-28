package com.resource.management.resource.model;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Equipment {

    private String equipmentId;

    private ResourceType resourceType;

    private String equipmentType;

    private Integer usable;

    private Integer unusable;

    private Integer reserves;
}
