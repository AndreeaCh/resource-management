package com.resource.management.api.resources.status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.resource.management.resource.model.ResourceStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateResourceStatusRequest
{
   private String id;

   private ResourceStatus resourceStatus;
}
