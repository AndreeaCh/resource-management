package com.resource.management.locations.model;

import java.util.ArrayList;
import java.util.List;

public class PointOfInterestMapper
{
   private static final PointOfInterest toInternal(
         final com.resource.management.api.resources.PointOfInterest externalPointOfInterest )
   {
      final PointOfInterest internalPointOfInterest = new PointOfInterest();
      internalPointOfInterest.setId( externalPointOfInterest.getId() );
      internalPointOfInterest.setName( externalPointOfInterest.getName() );
      return internalPointOfInterest;
   }


   public static final List<PointOfInterest> toInternal(
         final List<com.resource.management.api.resources.PointOfInterest> externalList )
   {
      final List<PointOfInterest> internalList = new ArrayList<>();
      for ( final com.resource.management.api.resources.PointOfInterest pointOfInterest : externalList )
      {
         internalList.add( toInternal( pointOfInterest ) );
      }
      return internalList;
   }


   private static final com.resource.management.api.resources.PointOfInterest toApi(
         final PointOfInterest internalPointOfInterest )
   {
      final com.resource.management.api.resources.PointOfInterest externalPointOfInterest =
            new com.resource.management.api.resources.PointOfInterest();
      externalPointOfInterest.setId( internalPointOfInterest.getId() );
      externalPointOfInterest.setName( internalPointOfInterest.getName() );
      return externalPointOfInterest;
   }


   public static final List<com.resource.management.api.resources.PointOfInterest> toApi(
         final List<PointOfInterest> internalList )
   {
      final List<com.resource.management.api.resources.PointOfInterest> externalList = new ArrayList<>();
      for ( final PointOfInterest pointOfInterest : internalList )
      {
         externalList.add( toApi( pointOfInterest ) );
      }
      return externalList;
   }
}
