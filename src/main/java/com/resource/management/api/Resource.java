package com.resource.management.api;

import com.resource.management.model.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    @Id
    private String plateNumber;

    private String vehicleType;

    private String captain;

    private String identificationNumber;

    private List<String> crew;

    private ResourceStatus status;

    private ResourceType type;
}
