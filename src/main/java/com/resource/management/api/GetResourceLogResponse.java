package com.resource.management.api;

import java.util.List;

import com.resource.management.data.ResourceLog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetResourceLogResponse
{
   private List<ResourceLog> resourceLogs;

}
