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
      switch ( day )
      {
         case "TODAY":
            timestampToday = this.timestampRepository.saveTodaysTimestamp();
            timestampTomorrow = this.timestampRepository.getTomorrowTimestamp();
            break;
         case "TOMORROW":
            timestampToday = this.timestampRepository.getTodaysTimestamp();
            timestampTomorrow = this.timestampRepository.saveTomorrowsTimestamp();
            break;
         default:
            timestampToday = this.timestampRepository.getTodaysTimestamp();
            timestampTomorrow = this.timestampRepository.getTomorrowTimestamp();
      }

      return new ServicesListUpdatedNotification( this.repository.findAll(),
            timestampToday == null ? Instant.now().toString() : timestampToday.getTimeStamp(),
            timestampTomorrow == null ? Instant.now().toString() : timestampTomorrow.getTimeStamp() );
   }


   public ServicesListUpdatedNotification getLastUpdatedNotification()
   {
      final LastUpdatedTimestamp timestampToday = this.timestampRepository.getTodaysTimestamp();
      final LastUpdatedTimestamp timestampTomorrow = this.timestampRepository.getTomorrowTimestamp();

      return new ServicesListUpdatedNotification( this.repository.findAll(),
            timestampToday == null ? Instant.now().toString() : timestampToday.getTimeStamp(),
            timestampTomorrow == null ? Instant.now().toString() : timestampTomorrow.getTimeStamp() );
   }
}
