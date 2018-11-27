package com.resource.management.api.status;

import com.resource.management.api.ResourceLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetResourceLogResponse {
    private List<ResourceLog> resourceLogs;
}
