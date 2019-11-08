package com.resource.management.locations.controller;

import java.util.ArrayList;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import com.resource.management.locations.model.Location;

public class Locations
{

   public static Location api()
   {
      return new Location( UUID.randomUUID().toString(), RandomStringUtils.randomAlphabetic( 1, 100 ),
            new Double[] { RandomUtils.nextDouble(), RandomUtils.nextDouble() }, new ArrayList<>() );
   }
}
