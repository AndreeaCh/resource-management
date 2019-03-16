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
        IN_MISSION, UNAVAILABLE, AVAILABLE, OPERATIONAL, NONOPERATIONAL;

        @Override
        public String toString() {
            switch (this){
                case IN_MISSION: return "Misiune";
                case AVAILABLE: return "Disponibil";
                case UNAVAILABLE: return "Indisponibil";
                case OPERATIONAL: return "Operațional";
                case NONOPERATIONAL: return "Neoperațional";
                default: return this.name();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("status='").append(status).append("'");
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
