package com.resource.management.locations.model;

public class LocationMapper
{

   public static final Location toInternal( final com.resource.management.api.resources.Location externalLocation )
   {
      final Location internalLocation = new Location();
      internalLocation.setId( externalLocation.getId() );
      internalLocation.setName( externalLocation.getName() );
      internalLocation.setCoordinates( externalLocation.getCoordinates() );
      internalLocation
            .setPointsOfInterest( PointOfInterestMapper.toInternal( externalLocation.getPointsOfInterest() ) );
      return internalLocation;
   }


   public static final com.resource.management.api.resources.Location toApi( final Location internalLocation )
   {
      final com.resource.management.api.resources.Location externalLocation =
            new com.resource.management.api.resources.Location();
      externalLocation.setId( internalLocation.getId() );
      externalLocation.setName( internalLocation.getName() );
      externalLocation.setCoordinates( internalLocation.getCoordinates() );
      externalLocation.setPointsOfInterest( PointOfInterestMapper.toApi( internalLocation.getPointsOfInterest() ) );
      return externalLocation;
   }
}
