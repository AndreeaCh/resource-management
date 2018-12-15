package com.resource.management.resource.model;

import java.util.UUID;
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
public class Equipment {

    @Id
    private String equipmentId;

    private ResourceType resourceType;

    private String equipmentType;

    private Integer usable;

    private Integer unusable;

    private Integer reserves;
}
