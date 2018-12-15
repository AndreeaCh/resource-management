package com.resource.management.api.resources;

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
    private String name;

    private List<Resource> resources;

    private List<Equipment> equipment;

    @EqualsAndHashCode.Exclude
    private String lastUpdate;

    @EqualsAndHashCode.Exclude
    private Boolean isLocked;
}
