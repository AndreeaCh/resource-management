package com.resource.management.resource.model;

import java.util.List;
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
public class Resource {
    @Id
    private String id;

    private String plateNumber;

    private String vehicleType;

    private String captain;

    private String identificationNumber;

    private List<String> crew;

    private ResourceType type;

    @EqualsAndHashCode.Exclude
    private ResourceStatus status;

    @EqualsAndHashCode.Exclude
    private List<ResourceLog> resourceLogs;
}
