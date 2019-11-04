package com.resource.management.locations.model;

import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Location
{
   @Id
   private String id;

   private String name;

   private Double[] coordinates;

   private List<PointOfInterest> pointsOfInterest;

}
