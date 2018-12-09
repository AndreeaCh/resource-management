package com.resource.management.api.resources.crud;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DeleteSubUnitRequest {
    private String name;
}
