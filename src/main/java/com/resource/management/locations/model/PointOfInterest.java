package com.resource.management.locations.model;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PointOfInterest
{
   @Id
   private String id;

   private String name;

   private String contact;

   private String comments;
}
