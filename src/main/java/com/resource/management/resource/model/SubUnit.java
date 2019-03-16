package com.resource.management.resource.model;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

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
