package com.resource.management.api.resources.lock;

import com.resource.management.api.resources.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LockSubUnitResponse {
    private StatusCode statusCode;
}
