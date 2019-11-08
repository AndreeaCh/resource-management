package com.resource.management.api.locations;

import java.util.List;

import com.resource.management.locations.model.PointOfInterest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddLocationRequest
{
   private String name;

   private Double[] coordinates;

   private List<PointOfInterest> pointsOfInterest;
}
