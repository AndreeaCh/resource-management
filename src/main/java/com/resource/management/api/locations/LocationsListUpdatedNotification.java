package com.resource.management.api.locations;

import java.util.List;

import com.resource.management.locations.model.Location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LocationsListUpdatedNotification
{
   private List<Location> locations;

}
