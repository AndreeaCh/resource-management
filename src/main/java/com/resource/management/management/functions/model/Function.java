package com.resource.management.management.functions.model;

import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Function {
    @Id
    private String id;
    private String name;
}
