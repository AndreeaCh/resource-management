package com.resource.management.api.status;

import com.resource.management.api.ResourceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateResourceStatusRequest {
    private String plateNumber;

    private ResourceStatus resourceStatus;
}
