package com.resource.management.services.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.springframework.data.annotation.Id;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LastUpdatedTimestamp
{
   @Id
   private String id;

   private String timeStamp;
}
