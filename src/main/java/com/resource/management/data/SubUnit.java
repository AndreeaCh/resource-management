package com.resource.management.data;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Data
@ToString
@AllArgsConstructor
public class SubUnit {
    @Id
    private String name;

    private List<Resource> resources;

    private String lastUpdate;

    private boolean isLocked;
}
