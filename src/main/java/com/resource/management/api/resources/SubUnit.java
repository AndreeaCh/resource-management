package com.resource.management.api.resources;

import com.resource.management.resource.model.ResourceType;
import java.util.Map;
import java.util.Set;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;

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
}
