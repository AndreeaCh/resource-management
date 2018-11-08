package com.resource.management.data;

import java.util.Date;

import lombok.Data;

@Data
public class ResourceLog
{
   private Date time;

   private ResourceStatus newStatus;
}
