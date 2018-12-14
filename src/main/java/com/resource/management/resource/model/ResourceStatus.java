package com.resource.management.resource.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class ResourceStatus {
    private Status status;

    private String key;

    private String description;

    private List<String> crew;

    public ResourceStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        IN_MISSION, UNAVAILABLE, AVAILABLE
    }

    @Override
    public String toString() {
        return "status=" + status +
                ", cheie='" + key + '\'' +
                ", descriere='" + description + '\'' +
                ", echipaj=" + crew;
    }
}
