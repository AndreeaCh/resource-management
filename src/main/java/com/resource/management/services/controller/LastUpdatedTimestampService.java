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
   private static final String TIME_STAMP_TOMORROW = "timeStampTomorrow";

   private static final String TIME_STAMP_TODAY = "timeStampToday";

   @Autowired
   private ServiceRepository repository;

   @Autowired
   private LastUpdatedTimestampRepository timestampRepository;


   public ServicesListUpdatedNotification getLastUpdatedNotification( final String day )
   {
      LastUpdatedTimestamp timestampToday = null;
      LastUpdatedTimestamp timestampTomorrow = null;
      switch ( day )
      {
         case "TODAY":
            timestampToday = new LastUpdatedTimestamp( TIME_STAMP_TODAY, Instant.now().toString() );
            timestampTomorrow = this.timestampRepository.getTomorrowTimestamp();
            this.timestampRepository.save( timestampToday );
            break;
         case "TOMORROW":
            timestampToday = this.timestampRepository.getTodaysTimestamp();
            timestampTomorrow = new LastUpdatedTimestamp( TIME_STAMP_TOMORROW, Instant.now().toString() );
            this.timestampRepository.save( timestampTomorrow );
            break;
         default:
            timestampToday = this.timestampRepository.getTodaysTimestamp();
            timestampTomorrow = this.timestampRepository.getTomorrowTimestamp();
      }

      return new ServicesListUpdatedNotification( this.repository.findAll(),
            timestampToday == null ? Instant.now().toString() : timestampToday.getTimeStamp(),
            timestampTomorrow == null ? Instant.now().toString() : timestampTomorrow.getTimeStamp() );
   }
}
