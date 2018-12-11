package com.resource.management.services.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Service {
    @Id
    private String id;
    private String name;
    private String title;
    private String role;
    private String contact;
    @JsonIgnore
    private String lastUpdate;
}
