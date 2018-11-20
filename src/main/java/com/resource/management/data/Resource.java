package com.resource.management.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String plateNumber;

    private String vehicleType;

    private String captain;

    private int identificationNumber;

    private List<String> crew;

    private ResourceStatus status;

    @JsonIgnore
    private List<ResourceLog> resourceLogs;
}
