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
package com.resource.management.services.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface LastUpdatedTimestampRepository extends MongoRepository<LastUpdatedTimestamp, String>
{

   @Query("{ '_id' : 'timeStampToday' }")
   LastUpdatedTimestamp getTodaysTimestamp();


   @Query("{ '_id' : 'timeStampTomorrow' }")
   LastUpdatedTimestamp getTomorrowTimestamp();
}
