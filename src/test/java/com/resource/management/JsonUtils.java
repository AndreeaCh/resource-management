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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtils
{
   private static final Gson GSON = new GsonBuilder().create();


   public static <T> T loadFromJsonString( final Class<T> configType, final String stringToBeParsed )
   {
      return JsonUtils.createJsonObject( configType, stringToBeParsed );
   }


   public static <T> T createJsonObject( final Class<T> clazz, final String jsonString )
   {
      return JsonUtils.GSON.fromJson( jsonString, clazz );
   }
}
