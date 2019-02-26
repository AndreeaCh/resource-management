package com.resource.management.management.functions.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Function {
    @Id
    private UUID id;
    private String name;
}
