package com.resource.management.api;

import java.util.List;

import com.resource.management.data.ResourceLog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class SubscribeResourceLogResponse
{
   private List<ResourceLog> resourceLogs;

}
