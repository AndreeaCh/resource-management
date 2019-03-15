package com.resource.management.resource.model.configuration;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SubUnitsConfiguration {
    public static final String ID = "SubUnitsConfiguration";
    @Id
    private String id;
    private List<String> orderedSubUnitIds;
}
