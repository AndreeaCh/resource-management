package com.resource.management.model.status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class ResourceStatus
{
   private Status status;

   public enum Status
   {
      IN_MISSION, UNAVAILABLE, AVAILABLE
   }
}
