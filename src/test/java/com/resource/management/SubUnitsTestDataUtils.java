/************************************************************************
 ** PROJECT:   XVP
 ** LANGUAGE:  Java, J2SE JDK 1.8
 **
 ** COPYRIGHT: FREQUENTIS AG
 **            Innovationsstrasse 1
 **            A-1100 VIENNA
 **            AUSTRIA
 **            tel +43 1 811 50-0
 **
 ** The copyright to the computer program(s) herein
 ** is the property of Frequentis AG, Austria.
 ** The program(s) shall not be used and/or copied without
 ** the written permission of Frequentis AG.
 **
 ************************************************************************/
package com.resource.management;

import java.util.List;
import java.util.stream.Collectors;

import com.resource.management.data.SubUnit;

public class SubUnitsTestDataUtils
{
   public static SubUnit loadRandomSubUnit()
   {
      return JsonUtils.loadFromJsonString( SubUnit.class, SubUnitsTestData.SUBUNIT1 );
   }


   public static List<SubUnit> loadAllSubUnits()
   {
      return SubUnitsTestData.ALL_UNITS.stream()
            .map( subUnit -> JsonUtils.loadFromJsonString( SubUnit.class, subUnit ) ).collect( Collectors.toList() );
   }

}
