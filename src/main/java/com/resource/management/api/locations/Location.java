package com.resource.management.api.locations;

import java.util.List;

import com.resource.management.api.resources.PointOfInterest;
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
