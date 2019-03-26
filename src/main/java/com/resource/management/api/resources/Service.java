package com.resource.management.api.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Service {

    @Id
    private String id;
    private String name;
    private String title;
    private String role;
    private String contact;
    private String day;
}
