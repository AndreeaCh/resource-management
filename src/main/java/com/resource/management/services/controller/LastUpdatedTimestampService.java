package com.resource.management.services.controller;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resource.management.api.services.ServicesListUpdatedNotification;
import com.resource.management.services.model.LastUpdatedTimestamp;
import com.resource.management.services.model.LastUpdatedTimestampRepository;
import com.resource.management.services.model.ServiceRepository;

@Service
public class LastUpdatedTimestampService
{
   @Autowired
   private ServiceRepository repository;

   @Autowired
   private LastUpdatedTimestampRepository timestampRepository;


   public ServicesListUpdatedNotification getLastUpdatedNotification( final String day )
   {
      LastUpdatedTimestamp timestampToday = null;
      LastUpdatedTimestamp timestampTomorrow = null;
      if ( "TODAY".equals( day ) )
      {
         timestampToday = new LastUpdatedTimestamp( "timeStampToday", Instant.now().toString() );
         timestampTomorrow = this.timestampRepository.findById( "timeStampTomorrow" ).orElse( null );
         this.timestampRepository.save( timestampToday );
      }
      else if ( "TOMORROW".equals( day ) )
      {
         timestampToday = this.timestampRepository.findById( "timeStampToday" ).orElse( null );
         timestampTomorrow = new LastUpdatedTimestamp( "timeStampTomorrow", Instant.now().toString() );
         this.timestampRepository.save( timestampTomorrow );
      }
      else
      {
         timestampToday = this.timestampRepository.findById( "timeStampToday" ).orElse( null );
         timestampTomorrow = this.timestampRepository.findById( "timeStampTomorrow" ).orElse( null );
      }

      return new ServicesListUpdatedNotification( this.repository.findAll(),
            timestampToday == null ? Instant.now().toString() : timestampToday.getTimeStamp(),
            timestampTomorrow == null ? Instant.now().toString() : timestampTomorrow.getTimeStamp() );
   }
}
