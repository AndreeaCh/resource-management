package com.resource.management.api;

import com.resource.management.model.ResourceStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ResourceLog
{
   private String changedAt;

   private String changedBy;

   private ResourceStatus status;
}
