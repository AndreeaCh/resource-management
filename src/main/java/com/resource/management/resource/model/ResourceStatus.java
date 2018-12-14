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
        StringBuilder stringBuilder = new StringBuilder();
        String statusAsString
                = status.equals(Status.IN_MISSION) ? "Misiune" : status.equals(Status.AVAILABLE) ? "Disponibil" : "Indisponibil";
        stringBuilder.append("status='").append(statusAsString).append("'");
        if (key != null && !key.isEmpty()) {
            stringBuilder.append(", cheie='").append(key).append("'");
        }
        if (description != null && !description.isEmpty()) {
            stringBuilder.append(", descriere='").append(description).append("'");
        }
        if (crew != null && !crew.isEmpty()) {
            stringBuilder.append(", echipaj='").append(crew).append("'");
        }

        return stringBuilder.toString();
    }
}
