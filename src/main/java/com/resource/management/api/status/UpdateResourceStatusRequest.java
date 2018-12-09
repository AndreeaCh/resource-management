package com.resource.management.api.status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.resource.management.model.ResourceStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateResourceStatusRequest
{
   private String plateNumber;

   private ResourceStatus resourceStatus;
}
