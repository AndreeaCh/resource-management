package com.resource.management.model;

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

    @EqualsAndHashCode.Exclude
    private String lastUpdate;

    @EqualsAndHashCode.Exclude
    private String lockedBy;

    @EqualsAndHashCode.Exclude
    private Boolean isLocked;
}
