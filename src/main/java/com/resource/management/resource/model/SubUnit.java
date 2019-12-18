package com.resource.management.resource.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Map;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SubUnit {
    @Id
    private String id;

    private String name;

    private List<Resource> resources;

    private List<Equipment> equipment;

    @EqualsAndHashCode.Exclude
    private String lastUpdateFirstInterventionResource;

    @EqualsAndHashCode.Exclude
    private String lastUpdateOtherResource;

    @EqualsAndHashCode.Exclude
    private String lastUpdateEquipment;

    @EqualsAndHashCode.Exclude
    private String lastUpdateReserveResource;

    @EqualsAndHashCode.Exclude
    private Map<String, ResourceType> lockedResourceTypeBySessionId;
}
