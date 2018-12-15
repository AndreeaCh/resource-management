package com.resource.management.api.resources;

import com.resource.management.resource.model.ResourceType;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Equipment {
    @Id
    private String equipmentId;

    private ResourceType resourceType;

    private String equipmentType;

    private Integer usable;

    private Integer unusable;

    private Integer reserves;
}
