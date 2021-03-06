package com.resource.management.api.management.subunits;

import com.resource.management.api.resources.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddSubUnitResponse {
    private StatusCode statusCode;
}
