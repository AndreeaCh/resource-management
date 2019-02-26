package com.resource.management.management.functions.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Functions {
    public static final String ID = "Functions_List";
    @Id
    private String id;
    private List<Function> functions;
}
