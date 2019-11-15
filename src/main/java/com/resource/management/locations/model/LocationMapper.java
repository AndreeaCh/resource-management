package com.resource.management.locations.model;

public class LocationMapper
{

   public static final Location toInternal( final com.resource.management.api.locations.Location externalLocation )
   {
      final Location internalLocation = new Location();
      internalLocation.setId( externalLocation.getId() );
      internalLocation.setName( externalLocation.getName() );
      internalLocation.setCoordinates( externalLocation.getCoordinates() );
      internalLocation.setPointsOfInterest( externalLocation.getPointsOfInterest() );
      return internalLocation;
   }


   public static final com.resource.management.api.locations.Location toApi( final Location internalLocation )
   {
      final com.resource.management.api.locations.Location externalLocation =
            new com.resource.management.api.locations.Location();
      externalLocation.setId( internalLocation.getId() );
      externalLocation.setName( internalLocation.getName() );
      externalLocation.setCoordinates( internalLocation.getCoordinates() );
      externalLocation.setPointsOfInterest( internalLocation.getPointsOfInterest() );
      return externalLocation;
   }
}
