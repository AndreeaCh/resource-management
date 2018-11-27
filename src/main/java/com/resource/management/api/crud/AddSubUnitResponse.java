package com.resource.management.api.crud;

import com.resource.management.api.StatusCode;
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
