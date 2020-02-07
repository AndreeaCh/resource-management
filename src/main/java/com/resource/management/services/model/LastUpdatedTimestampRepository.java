package com.resource.management.services.model;

import java.time.Instant;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface LastUpdatedTimestampRepository extends MongoRepository<LastUpdatedTimestamp, String>
{

   @Query("{ '_id' : 'timeStampToday' }")
   LastUpdatedTimestamp getTodaysTimestamp();


   @Query("{ '_id' : 'timeStampTomorrow' }")
   LastUpdatedTimestamp getTomorrowTimestamp();


   default LastUpdatedTimestamp saveTodaysTimestamp()
   {
      return this.save( new LastUpdatedTimestamp( "timeStampToday", Instant.now().toString() ) );
   }


   default LastUpdatedTimestamp saveTomorrowsTimestamp()
   {
      return this.save( new LastUpdatedTimestamp( "timeStampTomorrow", Instant.now().toString() ) );
   }
}
