package com.resource.management.locations.model;


import java.util.stream.Collectors;

public class LocationMapper {

    public static Location toInternal(final com.resource.management.api.locations.Location externalLocation) {
        final Location internalLocation = new Location();
        internalLocation.setId(externalLocation.getId());
        internalLocation.setName(externalLocation.getName());
        internalLocation.setCoordinates(externalLocation.getCoordinates());
        internalLocation.setPointsOfInterest(externalLocation.getPointsOfInterest().stream().map(LocationMapper::toInternal).collect(Collectors.toList()));
        return internalLocation;
    }

    public static PointOfInterest toInternal(com.resource.management.api.resources.PointOfInterest externalPointOfInterest) {
        final PointOfInterest internal = new PointOfInterest();
        internal.setId(externalPointOfInterest.getId());
        internal.setName(externalPointOfInterest.getName());
        internal.setContact(externalPointOfInterest.getContact());
        internal.setComments(externalPointOfInterest.getComments());
        return internal;
    }

    public static com.resource.management.api.locations.Location toApi(final Location internalLocation) {
        final com.resource.management.api.locations.Location externalLocation =
                new com.resource.management.api.locations.Location();
        externalLocation.setId(internalLocation.getId());
        externalLocation.setName(internalLocation.getName());
        externalLocation.setCoordinates(internalLocation.getCoordinates());
        externalLocation.setPointsOfInterest(internalLocation.getPointsOfInterest().stream().map(LocationMapper::toApi).collect(Collectors.toList()));
        return externalLocation;
    }

    public static com.resource.management.api.resources.PointOfInterest toApi(PointOfInterest externalPointOfInterest) {
        final com.resource.management.api.resources.PointOfInterest external =
                new com.resource.management.api.resources.PointOfInterest();
        external.setId(externalPointOfInterest.getId());
        external.setName(externalPointOfInterest.getName());
        external.setContact(externalPointOfInterest.getContact());
        external.setComments(externalPointOfInterest.getComments());
        return external;
    }
}
