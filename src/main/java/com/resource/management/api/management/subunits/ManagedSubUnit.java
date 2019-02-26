package com.resource.management.api.management.subunits;

import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ManagedSubUnit {
    @Id
    private String id;

    private String name;
}
