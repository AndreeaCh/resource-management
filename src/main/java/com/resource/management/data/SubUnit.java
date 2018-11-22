package com.resource.management.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SubUnit {
    @Id
    private String name;

    private List<Resource> resources;

    @EqualsAndHashCode.Exclude
    private String lastUpdate;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private String lockedBy;

    @EqualsAndHashCode.Exclude
    private boolean isLocked;
}
