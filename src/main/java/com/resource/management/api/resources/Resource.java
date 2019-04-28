package com.resource.management.api.resources;

import com.resource.management.resource.model.ResourceStatus;
import com.resource.management.resource.model.ResourceType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    @Id
    private String id;

    private String plateNumber;

    private String vehicleType;

    private String captain;

    private String identificationNumber;

    private List<String> crew;

    private ResourceStatus status;

    private ResourceType type;
}
