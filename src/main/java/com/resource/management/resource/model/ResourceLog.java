package com.resource.management.resource.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ResourceLog
{
   private UUID id;

   private String changedAt;

   private String changedBy;

   private ResourceStatus status;
}
