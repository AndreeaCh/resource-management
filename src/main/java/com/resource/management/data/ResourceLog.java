package com.resource.management.data;

import java.time.Instant;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class ResourceLog
{
   @Id
   private String id;

   private String plateNumber;

   private ResourceStatus newStatus;

   private Instant time;

}
