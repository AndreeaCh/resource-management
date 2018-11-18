package com.resource.management.data;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class SubUnit
{
   @Id
   private String name;

   private List<Resource> resources;

   private Instant lastUpdate;

}
