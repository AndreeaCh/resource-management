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
