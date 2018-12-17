package com.resource.management.resource.model;

import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private String name;

    private List<Resource> resources;

    private List<Equipment> equipment;

    @EqualsAndHashCode.Exclude
    private String lastUpdate;

    @EqualsAndHashCode.Exclude
    private Map<String, ResourceType> lockedResourceTypeBySessionId;
}
