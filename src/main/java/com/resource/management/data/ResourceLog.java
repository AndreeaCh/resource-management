package com.resource.management.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
public class ResourceLog
{
   @Id
   private String id;

   private String plateNumber;

   private ResourceStatus newStatus;

   private Instant time;

}
