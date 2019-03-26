package com.resource.management.services.model;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    private String day;
    @JsonIgnore
    private String lastUpdate;
}
