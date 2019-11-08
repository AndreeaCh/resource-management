package com.resource.management.api.locations;

import com.resource.management.api.resources.Location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateLocationRequest
{
   Location location;
}
