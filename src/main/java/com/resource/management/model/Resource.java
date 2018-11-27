package com.resource.management.model;

import com.resource.management.api.ResourceStatus;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Resource {
    @Id
    private String plateNumber;

    private String vehicleType;

    private String captain;

    private String identificationNumber;

    private List<String> crew;

    @EqualsAndHashCode.Exclude
    private ResourceStatus status;

    @EqualsAndHashCode.Exclude
    private List<ResourceLog> resourceLogs;
}
